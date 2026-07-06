package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.PostLike;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeleteLikeResponse {

    @JsonProperty("like_id")
    private Long likeId;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    public DeleteLikeResponse(PostLike like) {
        this.likeId = like.getLikeId();
        this.deletedAt = like.getDeletedAt();
    }
}