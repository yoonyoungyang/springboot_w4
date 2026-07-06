package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreatePostRequest {

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotBlank(message = "제목이 비어있습니다.")
    private String title;

    @NotNull(message = "내용이 비어있습니다.")
    private String content;

    @JsonProperty("content_img")
    private String contentImg;
}