package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class CreateLikeRequest {

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    public Long getUserId() {
        return userId;
    }
}