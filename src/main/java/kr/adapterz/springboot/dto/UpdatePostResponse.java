package kr.adapterz.springboot.dto;

import kr.adapterz.springboot.entity.Post;

import java.time.LocalDateTime;

public class UpdatePostResponse {
        private int post_id;
        private String title;
        private String content;
        private String content_img;
        private LocalDateTime updated_at;
        private int user_id;

        public UpdatePostResponse(Post post) {
            this.post_id = post.getPostId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.content_img = post.getContentImg();
            this.updated_at = post.getUpdatedAt();
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

        public LocalDateTime getUpdated_at() {
            return updated_at;
        }

        public int getUser_id() {
            return user_id;
        }


}
