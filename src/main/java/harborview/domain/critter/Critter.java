package harborview.domain.critter;

import java.util.ArrayList;
import java.util.List;

public class Critter {
    private int oid;
    private String name;
    private int sellVolume;

    private int status;
    private int critterType = 1;
    private int purchaseId;
    private int saleId;

    private List<AcceptRule> acceptrules;

    //----------------- oid -------------------
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    //----------------- status -------------------
    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    public CritterEnum getStatusEnum() {
        return CritterEnum.valueOf(status);
    }
    public void setStatusEnum(CritterEnum value) {
        status = value.getStatus();
    }
    //----------------- critterType -------------------
    public int getCritterType() {
        return critterType;
    }

    public void setCritterType(int value) {
        this.critterType = value;
    }

    //----------------- purchaseId -------------------
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int value) {
        this.purchaseId = value;
    }

    //----------------- saleId -------------------
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    //----------------- name -------------------
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------- sellVolume -------------------
    public int getSellVolume() {
        return sellVolume;
    }

    public void setSellVolume(int sellVolume) {
        this.sellVolume = sellVolume;
    }

    //----------------- acceptRules -------------------
    public List<AcceptRule> getAcceptRules() {
        if (acceptrules == null) {
            acceptrules = new ArrayList<AcceptRule>();
        }
        return acceptrules;
    }

    public void setAcceptRules(List<AcceptRule> acceptrules) {
        this.acceptrules = acceptrules;
    }
    public void addAcceptRule(AcceptRule acc) {
        if (acceptrules == null) {
            acceptrules = new ArrayList<AcceptRule>();
        }
        acceptrules.add(acc);
    }

    /*
    public boolean apply(SellRuleArgs args) {
        for (AcceptRule acc : getAcceptRules()) {
            if (acc.getActive().equals("n")) {
                //logger.info(String.format("[Acc %d - %s] Accept rule inactive."), acc.getOid(),acc.getRtypDesc());
                continue;
            }
            if (acc.pass(args) == true) {
                //logger.info(String.format("[Acc %d] Rule passes", acc.getOid()));
               return true;
            }
        }
        return false;
    }

     */

    public void inspect() {
        System.out.printf("\t[%d] Critter, status: %d, purchase id: %d, sale id: %d\n",
                oid, status, purchaseId, saleId);
        if (acceptrules == null || acceptrules.size() == 0) {
            System.out.println("\t\tNo accept rules");
        }
        else {
            for (AcceptRule acc : acceptrules) {
                acc.inspect();
            }
        }
    }

}
