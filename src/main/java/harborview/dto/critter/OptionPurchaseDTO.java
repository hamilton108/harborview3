package harborview.dto.critter;


import harborview.domain.stockmarket.StockOptionPurchase;
import harborview.domain.critter.Critter;

import java.util.ArrayList;
import java.util.List;

public class OptionPurchaseDTO {
    private final StockOptionPurchase purchase;

    public OptionPurchaseDTO(StockOptionPurchase purchase) {
        this.purchase = purchase;
    }

    public int getOid() {
        return purchase.getOid();
    }

    public String getTicker() {
        return purchase.getOptionName();
    }

    public List<CritterDTO> getCritters()  {
        List<CritterDTO> result = new ArrayList<>();
        for (Critter critter : purchase.getCritters()) {
            result.add(new CritterDTO(getOid(), critter));
        }
        return result;
    }

}
