package kr.adapterz.springboot.dto;

import kr.adapterz.springboot.entity.Post;

import java.time.LocalDateTime;

public class PostDetailResponse {
    private int post_id;
    private String title;
    private String content;
    private String content_img;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private int user_id;

    public PostDetailResponse(Post post) {
        this.post_id = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.content_img = post.getContentImg();
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
        this.user_id = post.getUserId();
    }

    public int getPost_id() {
        return post_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getContent_img() {
        return content_img;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public int getUser_id() {
        return user_id;
    }
}
