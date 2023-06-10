package harborview.dto;

public class ValueDTO<T> {
    private final T value;

    public ValueDTO(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
