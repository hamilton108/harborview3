package harborview.domain.critter;

import java.util.ArrayList;
import java.util.List;


public class AcceptRule extends AbstractRule {
    /*
    oid         | integer      | not null default nextval('accept_rules_oid_seq'::regclass)
    accValue       | price        | not null
    rtyp        | integer      | not null
    active      | bool_type    | default 'y'::bpchar
    name        | critter_name |
    description | rule_desc    |
    cid         | critter_id   |
    */
    private int oid;
    private int cid;
    private double accValue;
    private String rtypDesc;
    private String active = "y";
    private List<DenyRule> denyRules;

    public AcceptRule() {}

    public AcceptRule(int oid,
                      double accValue,
                      int rtyp,
                      String active) {
        this.oid = oid;
        this.accValue = accValue;
        this.rtyp = rtyp;
        this.active = active;
    }

    public AcceptRule(int oid,
                      double accValue,
                      int rtyp) {
        this(oid,accValue,rtyp,"y");
    }

    //---------------- oid -----------------
    public int getOid() {
        return oid;
    }
    public void setOid(int oid) {
        this.oid = oid;
    }

    //---------------- cid -----------------
    public int getCid() {
        return cid;
    }
    public void setCid(int value) {
        this.cid = value;
    }

    //---------------- accValue -----------------
    public double getAccValue() {
        return accValue;
    }

    public void setAccValue(double accValue) {
        this.accValue = accValue;
    }



    //---------------- active -----------------
    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public List<DenyRule> getDenyRules() {
        return denyRules;
    }

    public void setDenyRules(List<DenyRule> denyRules) {
        this.denyRules = denyRules;
    }
    public void addDenyRule(DenyRule dny) {
        if (denyRules == null) {
            denyRules = new ArrayList<DenyRule>();
        }
        denyRules.add(dny);
    }

    public String getRtypDesc() {
        if (rtypDesc == null) {
            rtypDesc = RuleTypeEnum.valueOf(rtyp).getDescription();
        }
        return rtypDesc;
    }

    public void setRtypDesc(String rtypDesc) {
        this.rtypDesc = rtypDesc;
    }


    private void logPass(double argVal) {
        /*
        logger.info(String.format("[Acc %d - %s] Pass: argVal %.2f > accValue %.2f",
                getOid(),
                getRtypDesc(),
                argVal,
                accValue));

         */
    }
    //---------------- pass -----------------
    /*
    public boolean pass(SellRuleArgs args) {
        if (denyRules != null) {
            for (DenyRule dny : denyRules) {
                if (dny.getActive().equals("n")) {
                    //logger.info(String.format("[Dny %d - %s] Deny rule inactive."), dny.getOid(),dny.getRtypDesc());
                    continue;
                }
                if (dny.block(args)) {
                    return false;
                }
            }
        }

        switch (getRtypEnum()) {
            case DFW: {
                if (args.getDiffFromWatermark() > accValue) {
                    logPass(args.getDiffFromWatermark());
                    return true;
                }
                return false;
            }
            case DFW_PCT: {
                return false;
            }
            case DFB: {
                if (args.getDiffFromBought() > accValue) {
                    logPass(args.getDiffFromBought());
                    return true;
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

     */

    public void inspect() {
        System.out.printf("\t\t[%d] Accept rule - rtyp: %s, accValue: %.2f, active: %s  \n", oid, getRtypDesc(), accValue, active);
        if (denyRules == null || denyRules.size() == 0) {
            System.out.println("\t\t\tNo deny rules");
        }
        else {
            for (DenyRule dny : denyRules) {
               dny.inspect();
            }
        }
    }
}
