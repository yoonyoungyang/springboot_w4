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

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    private boolean isLiked;

    public DeleteLikeResponse(PostLike like, boolean isLiked) {
        this.likeId = like.getLikeId();

        this.deletedAt = like.getDeletedAt();

        this.isLiked = isLiked;

    }
}