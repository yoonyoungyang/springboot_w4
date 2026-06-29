package kr.adapterz.springboot.entity;

import java.time.LocalDateTime;

public class Post {
    private int postId;
    private String title;
    private String content;
    private String contentImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int userId;


    public Post(int postId, String title, String content, String contentImg, int userId) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.contentImg = contentImg;
        this. createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }
    public int getUserId() {
        return userId;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getContentImg() {
        return contentImg;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void updatePost(String title, String content, String contentImg) {

        if (title != null) {
            this.title = title;
        }

        if (content != null) {
            this.content = content;
        }

        if (contentImg != null) {
            this.contentImg = contentImg;
        }

        this.updatedAt = LocalDateTime.now();
    }
}
