package kr.adapterz.springboot.dto;

import kr.adapterz.springboot.entity.Comment;

import java.time.LocalDateTime;

public class CreateCommentResponse {

    private int comment_id;
    private int post_id;
    private int user_id;
    private String content;
    private LocalDateTime created_at;

    public CreateCommentResponse(Comment comment) {
        this.comment_id = comment.getCommentId();
        this.post_id = comment.getPostId();
        this.user_id = comment.getUserId();
        this.content = comment.getContent();
        this.created_at = comment.getCreatedAt();
    }

    public int getComment_id() {
        return comment_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
}