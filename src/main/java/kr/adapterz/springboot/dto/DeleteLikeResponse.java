package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import kr.adapterz.springboot.entity.PostLike;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeleteLikeResponse {

    @NotNull
    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("like_id")
    private Long likeId;

    private boolean isLiked;

    public DeleteLikeResponse(PostLike like, boolean isLiked) {
        this.postId = like.getPost().getPostId();
        this.likeId = like.getLikeId();
        this.isLiked = isLiked;

    }
}