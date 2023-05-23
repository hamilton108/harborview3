package harborview.dto.html;

public class SelectItem {
    private final String t;
    private final String v;

    public SelectItem(String t, String v) {
        this.t = t;
        this.v = v;
    }

    public String getT() {
        return t;
    }

    public String getV() {
        return v;
    }
}
