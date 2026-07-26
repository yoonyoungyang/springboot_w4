package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.CreateLikeResponse;
import kr.adapterz.springboot.dto.DeleteLikeResponse;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.entity.PostLike;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.LikeRepository;
import kr.adapterz.springboot.repository.PostRepository;
import kr.adapterz.springboot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository,
                       PostRepository postRepository,
                       UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CreateLikeResponse createLike(Long postId, Long loginUserId) {
        Post post = postRepository.findPostByPostIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("좋아요 달기 실패 - 게시글 없음."));

        User user = userRepository.findUserByUserIdAndDeletedAtIsNull(loginUserId)
                .orElseThrow(() -> new RuntimeException("좋아요 달기 실패 - 회원 없음."));

        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }

        PostLike like = PostLike.builder()
                .post(post)
                .user(user)
                .build();

        PostLike savedLike = likeRepository.save(like);

        post.increaseLikeCount();

        return new CreateLikeResponse(savedLike, true);
    }

    public DeleteLikeResponse deleteLike(Long postId, Long loginUserId) {
        Post post = postRepository.findPostByPostIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("좋아요 삭제 실패 - 게시글 없음."));

        User user = userRepository.findUserByUserIdAndDeletedAtIsNull(loginUserId)
                .orElseThrow(() -> new RuntimeException("좋아요 삭제 실패 - 회원 없음."));

        PostLike like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("좋아요를 누르지 않았습니다."));


        likeRepository.delete(like);
        post.decreaseLikeCount();

        return new DeleteLikeResponse(like, false );
    }
}