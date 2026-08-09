package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.adapterz.springboot.entity.Cinema;
import kr.adapterz.springboot.entity.PostType;
import lombok.Getter;

@Getter
public class CreatePostRequest {

    @NotBlank(message = "제목이 비어있습니다.")
    private String title;

    @NotNull(message = "내용이 비어있습니다.")
    private String content;

    @JsonProperty("content_img")
    private String contentImg;

    private Cinema cinema;

    @JsonProperty("movie_name")
    private String movieName;

    @JsonProperty("post_type")
    private PostType postType;
}
