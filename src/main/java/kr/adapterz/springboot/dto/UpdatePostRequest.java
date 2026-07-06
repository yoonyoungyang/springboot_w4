package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdatePostRequest {

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    private String title;

    private String content;

    @JsonProperty("content_img")
    private String contentImg;
}