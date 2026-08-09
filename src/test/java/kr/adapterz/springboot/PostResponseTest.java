package kr.adapterz.springboot;

import kr.adapterz.springboot.dto.PostListResponse;
import kr.adapterz.springboot.entity.Cinema;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.entity.PostType;
import kr.adapterz.springboot.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostResponseTest {

    @Test
    void postListResponseContainsFilterFields() {
        User user = User.builder()
                .email("author@example.com")
                .password("password")
                .nickname("작성자")
                .build();
        Post post = Post.builder()
                .user(user)
                .title("예매가 열렸습니다")
                .content("내용")
                .cinema(Cinema.YONGSAN_IMAX)
                .movieName("듄: 파트 2")
                .postType(PostType.OPEN)
                .build();

        PostListResponse response = new PostListResponse(post);

        assertThat(response.getCinema()).isEqualTo(Cinema.YONGSAN_IMAX);
        assertThat(response.getMovieName()).isEqualTo("듄: 파트 2");
        assertThat(response.getPostType()).isEqualTo(PostType.OPEN);
    }
}
