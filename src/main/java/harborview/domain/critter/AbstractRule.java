package harborview.domain.critter;

public abstract class AbstractRule {
    protected int rtyp;

    protected RuleTypeEnum rtypEnum;
    protected RuleTypeEnum getRtypEnum() {
        if (rtypEnum == null) {
            //rtypEnum = RuleType.values()[rtyp];
            rtypEnum = RuleTypeEnum.valueOf(rtyp);
        }
        return rtypEnum;
    }

    public int getRtyp() {
        return rtyp;
    }

    public void setRtyp(int rtyp) {
        this.rtyp = rtyp;
    }


}
