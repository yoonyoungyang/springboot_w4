package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdatePostRequest {

    @NotNull
    @JsonProperty("post_id")
    private Long postId;

    private String title;

    private String content;

    @JsonProperty("content_img")
    private String contentImg;
}