package kr.adapterz.springboot.dto;

import kr.adapterz.springboot.entity.Like;

import java.time.LocalDateTime;

public class CreateLikeResponse {

    private int like_id;
    private int post_id;
    private int user_id;
    private LocalDateTime created_at;

    public CreateLikeResponse(Like like) {
        this.like_id = like.getLikeId();
        this.post_id = like.getPostId();
        this.user_id = like.getUserId();
        this.created_at = like.getCreatedAt();
    }

    public int getLike_id() {
        return like_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
}