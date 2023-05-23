package harborview.core.maunaloa;

import harborview.adapter.NordnetAdapter;
import harborview.adapter.StockMarketAdapter;
import harborview.adapter.impl.StockMarketAdapterImpl;
import harborview.dto.html.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MaunaloaCore {

    private Logger logger = LoggerFactory.getLogger(StockMarketAdapterImpl.class);
    private final NordnetAdapter nordnetAdapter;
    private final StockMarketAdapter stockMarketAdapter;

    /*
    public Core(@Qualifier("comp1") NordnetAdapter nordnetAdapter) {
        this.nordnetAdapter = nordnetAdapter;
    }
     */
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
}
