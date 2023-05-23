package harborview.mybatis;

import harborview.domain.stockmarket.Stock;
import harborview.domain.stockmarket.StockPrice;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.util.List;

public interface StockMapper {
    List<Stock> selectStocks();

    List<StockPrice> selectStockPrices(@Param("tickerId") int tickerId,
                                       @Param("fromDx") Date fromDx);
}
