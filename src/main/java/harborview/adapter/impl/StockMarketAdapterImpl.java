package harborview.adapter.impl;

import harborview.adapter.StockMarketAdapter;
import harborview.domain.stockmarket.Stock;
import harborview.domain.stockmarket.StockOptionPurchase;
import harborview.domain.stockmarket.StockTicker;
import harborview.mybatis.CritterMapper;
import harborview.mybatis.MyBatisUtil;
import harborview.mybatis.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component()
@Profile("prod")
public class StockMarketAdapterImpl implements StockMarketAdapter {

    private Logger logger = LoggerFactory.getLogger(StockMarketAdapterImpl.class);
    private final MyBatisUtil myBatisUtil;
    private final Date fromDate;

    public StockMarketAdapterImpl(MyBatisUtil myBatisUtil,
                                  @Value("${adapter.stockmarket.from-date}") String fromDate) {
        this.myBatisUtil = myBatisUtil;
        this.fromDate =  java.sql.Date.valueOf(fromDate);
        logger.info(String.format("From date: %s", this.fromDate.toString()));
    }

    @Override
    public List<Stock> getStocks() {
        return (myBatisUtil.withSession(session -> {
            var mapper = session.getMapper(StockMapper.class);
            return mapper.selectStocks();
        }));

    }

    @Override
    public List getStockPrices(StockTicker ticker, LocalDate fromDx) {
        return (myBatisUtil.withSession(session -> {
            var mapper = session.getMapper(StockMapper.class);
            if (fromDx == null) {
                return mapper.selectStockPrices(ticker.oid(), fromDate);
            }
            else {
                return mapper.selectStockPrices(ticker.oid(), fromDate);
            }
        }));
    }

    @Override
    public List<StockOptionPurchase> activePurchasesWithCritters(int purchaseType) {
        return (myBatisUtil.withSession(session -> {
            var mapper = session.getMapper(CritterMapper.class);
            return mapper.activePurchasesWithCritters(purchaseType);
        }));
    }

    @Override
    public void toggleRule(int ruleId, boolean active, boolean isAccRule) {
       myBatisUtil.withSessionConsumer(session -> {
           var mapper = session.getMapper(CritterMapper.class);
           var isActive = active ? "y" : "n";
           if (isAccRule) {
               mapper.toggleAcceptRule(ruleId, isActive);
           }
           else {
               mapper.toggleDenyRule(ruleId, isActive);
           }
       });
    }
}
