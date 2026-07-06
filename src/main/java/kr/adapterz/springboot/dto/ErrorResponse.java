package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private String field;

    private String code;

    @JsonProperty("error_message")
    private String errorMessage;

    public ErrorResponse(String field, String code, String errorMessage) {
        this.field = field;
        this.code = code;
        this.errorMessage = errorMessage;
    }
}