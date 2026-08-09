package kr.adapterz.springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    @Size(max = 26, message = "제목은 최대 26자까지 가능합니다.")
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "cinema")
    private Cinema cinema;

    @Column(name = "movie_name")
    private String movieName;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;

    @Column(name = "content_img")
    private String contentImg;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "comment_count", nullable = false)
    private int commentCount = 0;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @Builder
    private Post(User user, String title, String content, String contentImg,
                 Cinema cinema, String movieName, PostType postType) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.contentImg = contentImg;
        this.cinema = cinema;
        this.movieName = movieName;
        this.postType = postType;
    }

    public void updatePost(String title, String content, String contentImg,
                           Cinema cinema, String movieName, PostType postType) {
        if (title != null) {
            this.title = title;
        }

        if (content != null) {
            this.content = content;
        }

        if (contentImg != null) {
            this.contentImg = contentImg;
        }

        if (cinema != null) {
            this.cinema = cinema;
        }

        if (movieName != null) {
            this.movieName = movieName;
        }

        if (postType != null) {
            this.postType = postType;
        }
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }
}
