package kr.adapterz.springboot.exception;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse> handlePostNotFound(
            PostNotFoundException exception
    ) {
        ErrorResponse error = new ErrorResponse(
                null,
                "POST_DETAIL_FAIL",
                exception.getMessage()
        );
        ApiResponse response = new ApiResponse<>(
                "post_detail_fail",
                null,
                List.of(error)
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }



}
