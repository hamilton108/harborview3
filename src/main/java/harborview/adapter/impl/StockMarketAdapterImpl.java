package harborview.adapter.impl;

import harborview.adapter.StockMarketAdapter;
import harborview.domain.stockmarket.Stock;
import harborview.mybatis.MyBatisUtil;
import harborview.mybatis.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component()
public class StockMarketAdapterImpl implements StockMarketAdapter {

    private Map<Integer,String> oid2str = Map.ofEntries(
            Map.entry(18,"AKSO"),
            Map.entry(27,"BAKKA"),
            Map.entry(26, "BWLPG"),
            Map.entry(19, "DNB"),
            Map.entry(20, "DNO"),
            Map.entry(2, "EQNR"),
            Map.entry(21, "GJF"),
            Map.entry(28, "GOGL"),
            Map.entry(1,  "NHY"),
            Map.entry(29, "NAS"),
            Map.entry(7,  "OBX"),
            Map.entry(9,  "ORK"),
            Map.entry(12, "PGS"),
            Map.entry(14, "STB"),
            Map.entry(23, "SUBC"),
            Map.entry(6,  "TEL"),
            Map.entry(16, "TGS"),
            Map.entry(17, "TOM"),
            Map.entry(3,  "YAR")
    );
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
        return (myBatisUtil.withSession((session) -> {
            var mapper = session.getMapper(StockMapper.class);
            return mapper.selectStocks();
        }));

    }

    @Override
    public List getStockPrices(int tickerId, LocalDate fromDx) {
        return (myBatisUtil.withSession((session) -> {
            var mapper = session.getMapper(StockMapper.class);
            if (fromDx == null) {
                return mapper.selectStockPrices(tickerId, fromDate);
            }
            else {
                return mapper.selectStockPrices(tickerId, fromDate);
            }
        }));
    }

    @Override
    public String oidToTicker(int oid) {
        return oid2str.get(oid);
    }

           /*
            (def ticker->oid
    {"AKSO" 18
        "BAKKA" 27
        "BWLPG" 26
        "DNB" 19
        "DNO" 20
        "EQNR" 2
        "GJF" 21
        "GOGL" 28
        "NHY" 1
        "NAS" 29
        "OBX" 7
        "ORK" 9
        "PGS" 12
        "STB" 14
        "SUBC" 23
        "TEL" 6
        "TGS" 16
        "TOM" 17
        "YAR" 3})

            */
}
