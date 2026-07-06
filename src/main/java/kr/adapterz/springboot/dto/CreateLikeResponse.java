package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.PostLike;

import java.time.LocalDateTime;

public class CreateLikeResponse {

    @JsonProperty("like_id")
    private Long likeId;

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public CreateLikeResponse(PostLike like) {
        this.likeId = like.getLikeId();
        this.postId = like.getPost().getPostId();
        this.userId = like.getUser().getUserId();
        this.createdAt = like.getCreatedAt();
    }

    public Long getLikeId() {
        return likeId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}