package harborview.core.critter;

import harborview.adapter.StockMarketAdapter;
import harborview.dto.critter.OptionPurchaseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CritterCore {
    private Logger logger = LoggerFactory.getLogger(CritterCore.class);

    private final StockMarketAdapter stockMarketAdapter;

    public CritterCore(StockMarketAdapter stockMarketAdapter) {
        this.stockMarketAdapter = stockMarketAdapter;
    }

    public List<OptionPurchaseDTO> activePurchasesWithCritters(int purchaseType) {
        var purchases = stockMarketAdapter.activePurchasesWithCritters(purchaseType);
        if (purchases == null) {
            logger.warn(String.format("Empty list for critters, purchaseType=%d", purchaseType));
            return Collections.emptyList();
        }
        return purchases.stream().map(OptionPurchaseDTO::new).
                collect(Collectors.toList());
    }

    public void toggleRule(int ruleId, boolean active, boolean isAccRule) {
        stockMarketAdapter.toggleRule(ruleId, active, isAccRule);
    }
}
