package huce.edu.vn.appdocsach.constants;

public enum DefaultError {
    UNEXPECTED_ERROR(500, "Lỗi không xác định");
    private final Integer code;
    private final String message;

    DefaultError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
