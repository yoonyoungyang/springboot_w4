package kr.adapterz.springboot.dto;

public class ErrorResponse {
    private String field;
    private String code;
    private String error_message;

    public ErrorResponse(String field, String code, String error_message) {
        this.field = field;
        this.code = code;
        this.error_message = error_message;
    }

    public String getField() {
        return field;
    }
    public String getCode() {
        return code;
    }
    public String getError_message() {
        return error_message;
    }
}
