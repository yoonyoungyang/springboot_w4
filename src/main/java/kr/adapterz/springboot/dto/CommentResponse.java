package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponse {

    @JsonProperty("comment_id")
    private Long commentId;

    private String content;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("user_id")
    private Long userId;

    private String nickname;

    @JsonProperty("profile_img")
    private String profileImg;

    @JsonProperty("is_mine")
    private boolean isMine;



    public CommentResponse(Comment comment, Long loginUserId) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.userId = comment.getUser().getUserId();
        this.nickname = comment.getUser().getNickname();
        this.profileImg = comment.getUser().getProfileImg();

        this.isMine = comment.getUser().getUserId().equals(loginUserId);

    }
}