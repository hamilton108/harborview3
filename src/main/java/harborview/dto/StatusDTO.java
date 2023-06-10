package harborview.dto;

public class StatusDTO {
    private final boolean ok;

    private final String msg;

    private final int statusCode;

    public StatusDTO(boolean ok, String msg, int statusCode) {
        this.ok = ok;
        this.msg = msg;
        this.statusCode = statusCode;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
