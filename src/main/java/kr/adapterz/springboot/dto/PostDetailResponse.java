package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDetailResponse {

    @JsonProperty("post_id")
    private Long postId;

    private String title;
    private String content;

    @JsonProperty("content_img")
    private String contentImg;

    @JsonProperty("view_count")
    private int viewCount;

    @JsonProperty("like_count")
    private int likeCount;

    @JsonProperty("comment_count")
    private int commentCount;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("profile_img")
    private String profileImg;

    public PostDetailResponse(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.contentImg = post.getContentImg();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getCommentCount();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.userId = post.getUser().getUserId();
        this.nickname = post.getUser().getNickname();
        this.profileImg = post.getUser().getProfileImg();
    }
}