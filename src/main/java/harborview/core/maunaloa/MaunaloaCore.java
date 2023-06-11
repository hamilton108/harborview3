package harborview.core.maunaloa;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import harborview.adapter.NordnetAdapter;
import harborview.adapter.StockMarketAdapter;
import harborview.adapter.StockMarketAdapterUtil;
import harborview.chart.ChartFactory;
import harborview.chart.ChartMonthFactory;
import harborview.chart.ChartWeekFactory;
import harborview.domain.nordnet.*;
import harborview.domain.stockmarket.StockOptionTicker;
import harborview.domain.stockmarket.StockPrice;
import harborview.domain.stockmarket.StockTicker;
import harborview.dto.html.Charts;
import harborview.dto.html.SelectItem;
import harborview.util.StockOptionUtil;
import oahu.exceptions.BinarySearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vega.financial.StockOption;
import vega.financial.calculator.OptionCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static vega.financial.StockOption.OptionType.CALL;

@Component
public class MaunaloaCore {

    private Logger logger = LoggerFactory.getLogger(MaunaloaCore.class);
    private final NordnetAdapter nordnetAdapter;
    private final StockMarketAdapter stockMarketAdapter;

    private final OptionCalculator optionCalculator;

    private final ChartFactory chartFactory = new ChartFactory();
    private final ChartWeekFactory chartWeekFactory = new ChartWeekFactory();
    private final ChartMonthFactory chartMonthFactory = new ChartMonthFactory();
    Cache<Integer, List<StockPrice>> stockPriceCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    Cache<Integer, List<RLine>> rlineCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    private List<SelectItem> stockTickers;

    public MaunaloaCore(NordnetAdapter nordnetAdapter,
                        StockMarketAdapter stockMarketAdapter,
                        OptionCalculator optionCalculator) {
        this.nordnetAdapter = nordnetAdapter;
        this.stockMarketAdapter = stockMarketAdapter;
        this.optionCalculator = optionCalculator;
    }

    //@Cacheable(value="stockTickers")
    public List<SelectItem> getStockTickers() {
        if (stockTickers == null) {
            logger.info("(getStockTickers) Empty cache");
            stockTickers = stockMarketAdapter.getStocks().stream().map(
                x -> new SelectItem(String.format("%s - %s",x.getTicker(),x.getCompanyName()),String.valueOf(x.getOid()))).collect(Collectors.toList());
        }
        return stockTickers;
    }

    private List<StockPrice> getPrices(StockTicker ticker) {
        var cached = stockPriceCache.getIfPresent(ticker.oid());
        if (cached == null) {
            logger.info(String.format("Populating the stockPrice cache for oid: %d", ticker));
            cached = stockMarketAdapter.getStockPrices(ticker, null);
            stockPriceCache.put(ticker.oid(), cached);
        }
        return cached;
    }

    private Charts charts(StockTicker ticker, ChartFactory factory) {
        var prices = getPrices(ticker);
        return factory.elmCharts(ticker, prices);
    }
    public Charts days(StockTicker ticker) {
        return charts(ticker, chartFactory);
    }
    public Charts weeks(StockTicker ticker) {
        return charts(ticker, chartWeekFactory);
    }
    public Charts months(StockTicker ticker) {
        return charts(ticker, chartMonthFactory);
    }


    public String demo() {
        var result = nordnetAdapter.demo(new StockOptionTicker("YAR3A528.02X"));
        System.out.println(result);
        return result;
    }
    //----------------------------- Puts, Calls--------------------------------
    public String calls(StockTicker stockTicker) {
        return nordnetAdapter.calls(stockTicker);
    }

    public String puts(StockTicker stockTicker) {
        return nordnetAdapter.puts(stockTicker);
    }

    //----------------------------- Risc Lines --------------------------------
    void saveRiscResult(int oid,
                        String ticker,
                        double bid,
                        double ask,
                        double riscValue,
                        double riscStockPrice,
                        double riscOptionPrice,
                        double breakEven) {
        var line = new RLine(oid, ticker, bid, ask, riscValue, riscStockPrice, riscOptionPrice, breakEven);

        var lines = rlineCache.getIfPresent(oid);
        if (lines == null)  {
            logger.info(String.format("Empty rline cache for oid: %d", oid));
            var newLines = new ArrayList<RLine>();
            newLines.add(line);
            rlineCache.put(oid, newLines);
        }
        else {
            logger.info(String.format("Found rline cache for oid: %d", oid));
            lines.add(line);
        }
    }

    RiscResponse calcRiscStockPrice(RiscRequest request) {

        var oid = StockOptionUtil.stockOptionInfoFromTicker(request.getTicker()).first();

        FindOptionResponse hit = nordnetAdapter.findOption(request.getTicker());

        if (hit == null) {
            return new RiscResponse(request.getTicker(), -1.0, RiscResponseStatus.COULD_NOT_FIND_OPTION_ERROR);
        }

        var riscAdjustedPrice = hit.getStockOption().getAsk() - request.getRiscValue();

        if (riscAdjustedPrice < 0) {
            return new RiscResponse(request.getTicker(), -1.0, RiscResponseStatus.RISC_ADJUSTED_PRICE_LESS_THAN_ZERO);
        }

        if (hit.getStockOption().getIvBid() < 0) {
            return new RiscResponse(request.getTicker(), -1.0, RiscResponseStatus.IV_LESS_THAN_ZERO);
        }

        double curStockPrice = 0.0;
        double curBreakEven = 0.0;
        try {
            // Option price
            curStockPrice = optionCalculator.stockPriceFor2(hit.getStockOption().getOptionType(),
                    riscAdjustedPrice,
                    hit.getStockOption().getX(),
                    hit.getStockOption().getDays(),
                    hit.getStockOption().getIvBid(),
                    hit.getStockPrice().getClose());
        } catch (BinarySearchException ex) {
            return new RiscResponse(request.getTicker(), -1.0, RiscResponseStatus.CALCULATE_OPTION_PRICE_ERROR);
        }

        try {
            curBreakEven = optionCalculator.stockPriceFor2(hit.getStockOption().getOptionType(),
                    hit.getStockOption().getAsk(),
                    hit.getStockOption().getX(),
                    hit.getStockOption().getDays(),
                    hit.getStockOption().getIvBid(),
                    hit.getStockPrice().getClose());
        } catch (BinarySearchException ex) {
            return new RiscResponse(request.getTicker(), -1.0, RiscResponseStatus.BREAK_EVEN_ERROR);
        }

        var result = new RiscResponse(request.getTicker(), curStockPrice, RiscResponseStatus.OK);

        saveRiscResult(oid,
                hit.getStockOption().getTicker(),
                hit.getStockOption().getBid(),
                hit.getStockOption().getAsk(),
                request.getRiscValue(),
                curStockPrice,
                riscAdjustedPrice,
                curBreakEven);

        return result;
    }

    public List<RiscResponse> calcRiscStockPrices(List<RiscRequest> request) {
        var result = new ArrayList<RiscResponse>();
        for (var risc : request) {
            result.add(calcRiscStockPrice(risc));
        }
        return result;
    }

    public List<RLine> getRiscLines(StockTicker ticker) {
        var lines = rlineCache.getIfPresent(ticker.oid());
        if (lines == null) {
            return Collections.emptyList();
        }
        else {
            return lines;
        }
    }

    public void invalidateRiscLineCache() {
        rlineCache.invalidateAll();
    }
    public void invalidateStockPriceCache() {
        stockPriceCache.invalidateAll();
    }

    public double optionPriceFor(StockOptionTicker ticker, double stockPrice) {
        var info = StockOptionUtil.stockOptionInfoFromTicker(ticker);
        try {
            var option = nordnetAdapter.findOption(ticker).getStockOption();

            if (info.third() == CALL) {
                return optionCalculator.callPrice2(stockPrice, option.getX(), option.getDays(), option.getIvBid());
            } else {
                return optionCalculator.putPrice2(stockPrice, option.getX(), option.getDays(), option.getIvBid());
            }
        }
        catch (RuntimeException ex) {
            logger.error(ex.getMessage());
            return -1.0;
        }
    }

}
