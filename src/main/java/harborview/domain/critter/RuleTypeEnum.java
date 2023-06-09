package harborview.domain.critter;

/*
(def  DFW       1)  ;Diff from watermark
        (def  DFW-PCT   2)  ; Diff from watermark percent
        (def  SP-FLOOR  3)  ; Stock price floor (valid if current stock value above price, i.e. in the house)
        (def  SP-ROOF   4)  ; Stock price roof (valid if current stock value below price, i.e. in the house)
        (def  OP-FLOOR  5)  ; Option price floor (valid if current option value above price)
        (def  OP-ROOF   6)  ; Option price floor (valid if current option value below price)
        (def  DFB       7)  ; Diff from bought
        (def  DW-SLIDE  9)  ; Diff from watermark slide (gradient rule)
        (def  CALL      10)
        (def  PUT       11)
        (def  SPOT      12)
        (def  WM-SPOT   13)  ; Watermark spot
        (def  OPTION-PRICE  14)
*/

public enum RuleTypeEnum {
    DFW (1, "Diff from watermark"),
    DFW_PCT (2, "Diff from watermark percent"),
    SP_FLOOR  (3, "Stock price floor (valid if current stock value above price, i.e. in the house)"),
    SP_ROOF   (4, "Stock price roof (valid if current stock value below price, i.e. in the house)"),
    OP_FLOOR  (5, "Option price floor (valid if current option value above price)"),
    OP_ROOF   (6, "Option price roof (valid if current option value below price)"),
    DFB       (7,  "Diff from bought"),
    DW_SLIDE  (9,  "Diff from watermark slide (gradient rule)");
    RuleTypeEnum(int kind, String description) {
        this.kind = kind;
        this.description = description;
    }
    private final int kind;
    private final String description;

    public int getKind() {
        return kind;
    }

    public String getDescription() {
        return description;
    }

    public static RuleTypeEnum valueOf(int rtyp) {
        for (RuleTypeEnum r : RuleTypeEnum.values())  {
            if (r.kind == rtyp) {
                return r;
            }
        }
        return null;
    }
    /*
        (def  CALL      10)
            (def  PUT       11)
            (def  SPOT      12)
            (def  WM-SPOT   13)  ; Watermark spot
            (def  OPTION-PRICE  14)
            */
}
