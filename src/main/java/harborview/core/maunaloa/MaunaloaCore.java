package harborview.core.maunaloa;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import harborview.adapter.NordnetAdapter;
import harborview.adapter.StockMarketAdapter;
import harborview.adapter.impl.StockMarketAdapterImpl;
import harborview.chart.ChartFactory;
import harborview.chart.ChartMonthFactory;
import harborview.chart.ChartWeekFactory;
import harborview.domain.stockmarket.StockPrice;
import harborview.dto.html.Charts;
import harborview.dto.html.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MaunaloaCore {

    private Logger logger = LoggerFactory.getLogger(StockMarketAdapterImpl.class);
    private final NordnetAdapter nordnetAdapter;
    private final StockMarketAdapter stockMarketAdapter;

    private final ChartFactory chartFactory = new ChartFactory();
    private final ChartWeekFactory chartWeekFactory = new ChartWeekFactory();
    private final ChartMonthFactory chartMonthFactory = new ChartMonthFactory();
    Cache<Integer, List<StockPrice>> stockPriceCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            //.maximumSize(100)
            .build();

    public MaunaloaCore(NordnetAdapter nordnetAdapter,
                        StockMarketAdapter stockMarketAdapter) {
        this.nordnetAdapter = nordnetAdapter;
        this.stockMarketAdapter = stockMarketAdapter;
    }

    @Cacheable(value="stockTickers")
    public List<SelectItem> getStockTickers() {
        logger.info("(getStockTickers) Empty cache");
        return stockMarketAdapter.getStocks().stream().map(
                x -> new SelectItem(String.format("%s - %s",x.getTicker(),x.getCompanyName()),String.valueOf(x.getOid()))).collect(Collectors.toList());
    }

    private List<StockPrice> getPrices(int stockId) {
        var cached = stockPriceCache.getIfPresent(stockId);
        if (cached == null) {
            logger.info("Populating the stockPrice cache");
            cached = stockMarketAdapter.getStockPrices(stockId, null);
            stockPriceCache.put(stockId, cached);
        }
        return cached;
    }

    private Charts charts(int stockId, ChartFactory factory) {
        var prices = getPrices(stockId);
        return factory.elmCharts(stockMarketAdapter.oidToTicker(stockId), prices);
    }
    public Charts days(int stockId) {
        return charts(stockId, chartFactory);
    }
    public Charts weeks(int stockId) {
        return charts(stockId, chartWeekFactory);
    }
    public Charts months(int stockId) {
        return charts(stockId, chartMonthFactory);
    }

}
