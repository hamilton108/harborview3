package harborview.domain.critter;

public enum CritterEnum {
    NA(0, "Critter status not set"),
    OPTION_FULLY_SOLD(2, "Option fully sold"),
    ACTIVE (7, "Critter active"),
    INACTIVE (8, "Critter inactive"),
    CRITTER_SOLD(9, "Critter sold");

    private final int status;
    private final String description;

    CritterEnum (int status, String description) {
        this.status = status;
        this.description = description;
    }
    public static CritterEnum valueOf(int status) {
        for (CritterEnum r : CritterEnum.values())  {
            if (r.status == status) {
                return r;
            }
        }
        return null;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
