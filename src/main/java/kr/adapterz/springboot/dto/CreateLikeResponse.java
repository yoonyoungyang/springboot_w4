package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.PostLike;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateLikeResponse {

    @JsonProperty("like_id")
    private Long likeId;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    private boolean isLiked;


    public CreateLikeResponse(PostLike like, boolean isLiked) {
        this.likeId = like.getLikeId();
        this.postId = like.getPost().getPostId();
        this.userId = like.getUser().getUserId();
        this.createdAt = like.getCreatedAt();
        this.isLiked = isLiked;
    }
}