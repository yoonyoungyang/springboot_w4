package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.entity.Cinema;
import kr.adapterz.springboot.entity.PostType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdatePostResponse {

    @JsonProperty("post_id")
    private Long postId;

    private String title;

    private String content;
    private Cinema cinema;

    @JsonProperty("movie_name")
    private String movieName;

    @JsonProperty("post_type")
    private PostType postType;

    @JsonProperty("content_img")
    private String contentImg;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("user_id")
    private Long userId;

    public UpdatePostResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.cinema = post.getCinema();
        this.movieName = post.getMovieName();
        this.postType = post.getPostType();
        this.contentImg = post.getContentImg();
        this.updatedAt = post.getUpdatedAt();
        this.userId = post.getUser().getUserId();
    }
}
