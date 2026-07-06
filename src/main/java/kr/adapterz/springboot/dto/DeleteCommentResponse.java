package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeleteCommentResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    public DeleteCommentResponse(Comment comment) {
        this.commentId = comment.getCommentId();
        this.deletedAt = comment.getDeletedAt();
    }
}