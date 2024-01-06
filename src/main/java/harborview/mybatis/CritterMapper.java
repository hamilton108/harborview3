package harborview.mybatis;

import harborview.domain.critter.RuleType;
import harborview.domain.stockmarket.StockOptionPurchase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CritterMapper {
    void toggleAcceptRule(@Param("oid") int oid, @Param("isActive") String isActive);
    void toggleDenyRule(@Param("oid") int oid, @Param("isActive") String isActive);

    List<RuleType> ruleTypes();
    List<StockOptionPurchase> activePurchasesWithCritters(@Param("purchaseType") int purchaseType);

    /*
    void insertCritter(Critter critter);

    void insertGradientRule(GradientRule rule);

    void insertAcceptRule(AcceptRule rule);

    void insertDenyRule(DenyRule rule);

    List<RuleType> ruleTypes();

    void registerCritterClosedWithSale(Critter critter);

     */
}
