package harborview.dto.critter;

import harborview.domain.critter.AcceptRule;
import harborview.domain.critter.Critter;

import java.util.ArrayList;
import java.util.List;

public class CritterDTO {
    private final Critter critter;
    private final int purchaseId;
    public CritterDTO(int purchaseId, Critter critter) {
        this.purchaseId = purchaseId;
        this.critter = critter;
    }
    public int getOid() {
        return critter.getOid();
    }
    public int getVol() {
        return critter.getSellVolume();
    }
    public int getStatus() {
        return critter.getStatus();
    }
    public List<AccRuleDTO> getAccRules() {
        List<AccRuleDTO> result = new ArrayList<>();
        for (AcceptRule accRule :  critter.getAcceptRules()) {
            result.add(new AccRuleDTO(purchaseId,getOid(),accRule));
        }
        return result;
    }
}
