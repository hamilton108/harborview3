package harborview.dto;

public enum StatusCode {
    Ok(1),
    Error(2);

    private final int status;

    StatusCode (int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
