package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.CreateLikeRequest;
import kr.adapterz.springboot.dto.CreateLikeResponse;
import kr.adapterz.springboot.dto.DeleteLikeResponse;
import kr.adapterz.springboot.entity.Like;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.repository.LikeRepository;
import kr.adapterz.springboot.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository,
                       PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    public CreateLikeResponse createLike(int postId,
                                         CreateLikeRequest request) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }

        Like existLike =
                likeRepository.findLike(postId, request.getUser_id());

        if (existLike != null) {
            throw new RuntimeException("이미 좋아요를 눌렀습니다.");
        }

        int likeId = likeRepository.nextLikeId();

        Like like = new Like(
                likeId,
                postId,
                request.getUser_id()
        );

        Like savedLike = likeRepository.save(like);

        return new CreateLikeResponse(savedLike);
    }

    public DeleteLikeResponse deleteLike(int postId,
                                         int userId) {

        Like like =
                likeRepository.findLike(postId, userId);

        if (like == null) {
            throw new RuntimeException("좋아요가 존재하지 않습니다.");
        }

        likeRepository.deleteLike(like.getLikeId());

        return new DeleteLikeResponse(like.getLikeId());
    }
}