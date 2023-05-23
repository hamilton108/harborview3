package harborview.adapter.impl;

import harborview.adapter.StockMarketAdapter;
import harborview.domain.stockmarket.Stock;
import harborview.mybatis.MyBatisUtil;
import harborview.mybatis.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component()
public class StockMarketAdapterImpl implements StockMarketAdapter {

    private Logger logger = LoggerFactory.getLogger(StockMarketAdapterImpl.class);
    private final MyBatisUtil myBatisUtil;

    public StockMarketAdapterImpl(MyBatisUtil myBatisUtil) {
        this.myBatisUtil = myBatisUtil;
    }

    @Override
    public List<Stock> getStocks() {
        /*
        return MyBatisUtils.withSession((session) -> {
            var mapper = session.getMapper(GeneralJournalMapper.class);
            return mapper.selectByBilag(numItems);
        });

         */
        return (myBatisUtil.withSession((session) -> {
            var mapper = session.getMapper(StockMapper.class);
            return mapper.selectStocks();
        }));

    }
}
