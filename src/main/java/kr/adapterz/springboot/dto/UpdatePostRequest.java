package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import kr.adapterz.springboot.entity.Cinema;
import kr.adapterz.springboot.entity.PostType;
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

    private Cinema cinema;

    @JsonProperty("movie_name")
    private String movieName;

    @JsonProperty("post_type")
    private PostType postType;
}
