package kr.adapterz.springboot.entity;

import java.time.LocalDateTime;

public class Like {

    private int likeId;
    private int postId;
    private int userId;
    private LocalDateTime createdAt;

    public Like(int likeId, int postId, int userId) {
        this.likeId = likeId;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    public int getLikeId() {
        return likeId;
    }

    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}