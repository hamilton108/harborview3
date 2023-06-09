package harborview.mybatis;

import harborview.domain.critter.RuleType;
import harborview.domain.stockmarket.StockOptionPurchase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CritterMapper {
    void toggleAcceptRule(@Param("oid") int oid, @Param("isActive") String isActive);
    void toggleDenyRule(@Param("oid") int oid, @Param("isActive") String isActive);

    List<RuleType> ruleTypes();
    List<StockOptionPurchase> activePurchasesWithCritters(@Param("purchaseType") int purchaseType);
}
