package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CreatePostResponse createPost(CreatePostRequest request) {
        int postId = postRepository.nextPostId();

        Post post = new Post(postId, request.getTitle(), request.getContent(), request.getContent_img(), request.getUser_id());

        Post savedPost = postRepository.save(post);
        return new CreatePostResponse(savedPost);
    }

    public List<PostListResponse> postList() {
        List<Post> postList = postRepository.findAll();
        List<PostListResponse> responseList = new ArrayList<>();

        for (Post post : postList) {
            responseList.add(new PostListResponse(post));
        }

        return responseList;
    }

    public PostDetailResponse postDetail(int postId) {
        Post onePost = postRepository.findPostById(postId);

        if (onePost == null) {
            throw new RuntimeException("게시글 상세 조회 실패 - 게시글 없음");
        }
        return new PostDetailResponse(onePost);
    }



    public UpdatePostResponse updatePost(int postId,
                                         UpdatePostRequest request) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new RuntimeException("게시글 수정 불가 - 게시글 없음.");
        }

        if (post.getUserId() != request.getUser_id()) {
            throw new RuntimeException("게시글 수정 불가 - 게시글 수정 권한이 없음.");
        }

        post.updatePost(
                request.getTitle(),
                request.getContent(),
                request.getContent_img()
        );

        return new UpdatePostResponse(post);
    }


    public DeletePostResponse deletePost(int postId,
                                         int userId) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new RuntimeException("게시글 수정 불가 - 게시글 없음.");
        }

        if (post.getUserId() != userId) {
            throw new RuntimeException("게시글 수정 불가 - 게시글 수정 권한이 없음.");
        }

        postRepository.deletePost(postId);

        return new DeletePostResponse(postId);
    }
}
