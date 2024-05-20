package huce.edu.vn.appdocsach.constants;

public enum DefaultError {
    UNEXPECTED_ERROR(500, "Lỗi không xác định");
    private Integer code;
    private String message;

    private DefaultError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
