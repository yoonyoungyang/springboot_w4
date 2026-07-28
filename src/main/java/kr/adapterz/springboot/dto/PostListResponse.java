package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostListResponse {

    @JsonProperty("post_id")
    private Long postId;

    private String title;

    @JsonProperty("content_img")
    private String contentImg;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("view_count")
    private int viewCount;

    @JsonProperty("like_count")
    private int likeCount;

    @JsonProperty("comment_count")
    private int commentCount;

    @JsonProperty("author_nickname")
    private String authorNickname;

    @JsonProperty("author_profileImg")
    private String authorProfileImg;



    public PostListResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.contentImg = post.getContentImg();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.userId = post.getUser().getUserId();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.authorNickname = post.getUser().getNickname();
        this.authorProfileImg = post.getUser().getProfileImg();
    }
}