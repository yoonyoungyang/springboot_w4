package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.PostRepository;
import kr.adapterz.springboot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CreatePostResponse createPost(CreatePostRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("게시글 생성 실패 - 회원 없음. "));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .contentImg(request.getContentImg())
                .build();

        Post savedPost = postRepository.save(post);

        return new CreatePostResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public List<PostListResponse> postList() {
        return postRepository.findAll()
                .stream()
                .filter(post -> post.getDeletedAt() == null)
                .map(PostListResponse::new)
                .toList();
    }

    public PostDetailResponse postDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 상세 조회 실패 - 게시글 없음"));

        if (post.getDeletedAt() != null) {
            throw new RuntimeException("게시글 상세 조회 실패 - 삭제된 게시글");
        }

        post.increaseViewCount();

        return new PostDetailResponse(post);
    }

    public UpdatePostResponse updatePost(Long postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 수정 불가 - 게시글 없음."));

        if (!post.getUser().getUserId().equals(request.getUserId())) {
            throw new RuntimeException("게시글 수정 불가 - 게시글 수정 권한이 없음.");
        }

        post.updatePost(
                request.getTitle(),
                request.getContent(),
                request.getContentImg()
        );

        return new UpdatePostResponse(post);
    }

    public DeletePostResponse deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 삭제 불가 - 게시글 없음."));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("게시글 삭제 불가 - 게시글 삭제 권한이 없음.");
        }

        post.softDelete();

        return new DeletePostResponse(post);
    }
}