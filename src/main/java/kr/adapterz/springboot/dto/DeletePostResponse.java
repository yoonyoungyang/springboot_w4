package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeletePostResponse {

    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    public DeletePostResponse(Post post) {
        this.postId = post.getPostId();
        this.deletedAt = post.getDeletedAt();
    }
}