package kr.adapterz.springboot.common;


import kr.adapterz.springboot.dto.ErrorResponse;

import java.util.List;

public class ApiResponse<T> {
    private String message;
    private  T data;
    private List<ErrorResponse> errors;

    public ApiResponse(String message, T data, List<ErrorResponse> errors){
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public List<ErrorResponse> getErrors() {
        return errors;
    }
}
