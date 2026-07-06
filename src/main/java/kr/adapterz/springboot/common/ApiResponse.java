package kr.adapterz.springboot.common;

import kr.adapterz.springboot.dto.ErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ApiResponse<T> {

    private final String message;
    private final T data;
    private final List<ErrorResponse> errors;

    public ApiResponse(String message, T data, List<ErrorResponse> errors) {
        this.message = message;
        this.data = data;
        this.errors = errors;
    }
}