package harborview.core.maunaloa;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import harborview.adapter.NordnetAdapter;
import harborview.adapter.StockMarketAdapter;
import harborview.chart.ChartFactory;
import harborview.chart.ChartMonthFactory;
import harborview.chart.ChartWeekFactory;
import harborview.domain.nordnet.*;
import harborview.domain.stockmarket.StockOptionPurchase;
import harborview.domain.stockmarket.StockOptionSale;
import harborview.domain.stockmarket.StockOptionTicker;
import harborview.domain.stockmarket.StockPrice;
import harborview.domain.stockmarket.StockTicker;
import harborview.dto.StatusDTO;
import harborview.dto.html.Charts;
import harborview.dto.html.SelectItem;
import harborview.util.StockOptionUtil;
import oahu.dto.Tuple2;
import oahu.exceptions.BinarySearchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import vega.financial.calculator.OptionCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import harborview.dto.StatusCode;
import static vega.financial.StockOptionType.CALL;

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
        System.out.println(stockMarketAdapter);
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
            logger.info(String.format("Populating the stockPrice cache for oid: %d", ticker.oid()));
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


    //----------------------------- Puts, Calls, Spot --------------------------------
    public String calls(StockTicker stockTicker) {
        return nordnetAdapter.calls(stockTicker);
    }

    public String puts(StockTicker stockTicker) {
        return nordnetAdapter.puts(stockTicker);
    }

    public String spot(StockTicker stockTicker) {
        return nordnetAdapter.spot(stockTicker);
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
    public StatusDTO deleteAllRiscLines(StockTicker ticker) {
        rlineCache.invalidate(ticker.oid());
        return new StatusDTO(true, String.format("Deleted risc lines for %s ok", ticker.ticker()), StatusCode.OK.getStatus());
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


    public StatusDTO purchaseOption(StockOptionPurchase purchase) {
        var mh = new MyErrorHandler(StatusCode.PURCHASE_STOCK_OPTION_ERROR);
        stockMarketAdapter.insertPurchase(purchase, mh);
        if (mh.get() != null) {
            return mh.get();
        }
        return new StatusDTO(true, String.format("Purchased %s ok", purchase.getTicker()), StatusCode.OK.getStatus());
    }
    public StatusDTO sellOption(StockOptionSale sale) {
        var mh = new MyErrorHandler(StatusCode.SELL_STOCK_OPTION_ERROR);
        stockMarketAdapter.insertSale(sale, mh);
        if (mh.get() != null) {
            return mh.get();
        }
        return new StatusDTO(true, String.format("Sale for purchase id %d ok", sale.getPurchaseOid()), StatusCode.OK.getStatus());
    }

    private String insertSuccessMsg(String ticker, int oid) {
        return String.format("Inserted stockOption purchase ticker: %s, oid: %d", ticker, oid);
    }
    public StatusDTO registerAndPurchaseOption(Tuple2<harborview.domain.stockmarket.StockOption,StockOptionPurchase> purchase) {
        try {
            var mh = new MyErrorHandler(StatusCode.INSERT_STOCK_OPTION_ERROR);
            stockMarketAdapter.insertStockOption(purchase.first(), mh);
            if (mh.get() != null) {
                return mh.get();
            }
            logger.info(String.format("Inserted stockOption: %s", purchase.first().getTicker()));

            purchase.second().setOptionId(purchase.first().getOid());
            stockMarketAdapter.insertPurchase(purchase.second(), null);
            var msg = insertSuccessMsg(purchase.first().getTicker(), purchase.second().getOid());
            logger.info(msg);

            return new StatusDTO(true, msg, StatusCode.OK.getStatus());
        }
        catch (Exception ex) {
            logger.error(String.format("%s for stockOption purchase ticker: %s", ex.getMessage(), purchase.first().getTicker()));
            return new StatusDTO(false, ex.getMessage(), StatusCode.INSERT_DB_ERROR.getStatus());
        }
    }

    static class MyErrorHandler implements Consumer<Exception> {

        final AtomicReference<StatusDTO> status = new AtomicReference<>();
        final StatusCode statusCode;

        public MyErrorHandler(StatusCode statusCode) {
            this.statusCode = statusCode;
        }
        @Override
        public void accept(Exception e) {
            status.set(new StatusDTO(false, e.getMessage(), this.statusCode.getStatus()));
        }

        public StatusDTO get() {
            return status.get();
        }
    }

    /*
    public void demo() {
        var opt = new harborview.domain.stockmarket.StockOption();
        opt.setTicker("YAR3A459.57X");
        opt.setX(459.57);
        opt.setExpiry(java.time.LocalDate.of(2023,1,15));
        opt.setOpType(CALL);
        opt.setStockId(3);
        opt.setSeries("3A");

        stockMarketAdapter.insertStockOption(opt, mh);
        if (mh.get() != null) {
            System.out.println(mh.get());
        }
        System.out.println(opt);
    }

    static Function<Exception,StatusDTO> myHandler() {
        final AtomicReference<StatusDTO> status = new AtomicReference<>();
        return (err) -> {
            status.set(new StatusDTO(false, err.getMessage(), StatusCode.INSERT_DB_ERROR.getStatus()));
            return status.get();
        };
    }
    static Consumer<Exception> myHandler2() {
        final AtomicReference<StatusDTO> status = new AtomicReference<>();
        return (err) -> {
            status.set(new StatusDTO(false, err.getMessage(), StatusCode.INSERT_DB_ERROR.getStatus()));
        };
    }
     */

}
