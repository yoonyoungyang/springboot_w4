package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class CreateLikeRequest {

    @NotNull
    @JsonProperty("post_id")
    private Long postId;

}