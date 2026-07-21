package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateCommentRequest {

    @NotNull
    @JsonProperty("post_id")
    private Long postId;


    @NotBlank
    private String content;
}