package harborview.dto;

public enum StatusCode {
    Retry(1),
    Ok(2),
    Error(3);

    private final int status;

    StatusCode (int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
