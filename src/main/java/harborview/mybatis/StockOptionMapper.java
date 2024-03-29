package harborview.mybatis;

import harborview.domain.stockmarket.StockOption;
import harborview.domain.stockmarket.StockOptionPurchase;
import harborview.domain.stockmarket.StockOptionSale;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockOptionMapper {
    StockOption findStockOption(String ticker);
    void insertPurchase(StockOptionPurchase purchase);
    void insertSale(StockOptionSale sale);
    void insertStockOption(StockOption option);
    List<StockOptionPurchase> purchasesWithSalesAll(
            @Param("purchaseType") int purchaseType,
            @Param("status") int status,
            @Param("optype") String optype);
}
