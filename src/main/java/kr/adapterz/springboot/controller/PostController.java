package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ApiResponse<CreatePostResponse> createPost(@RequestBody CreatePostRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());


        if (loginUserId == null || loginUserId <= 0) {
            errors.add(new ErrorResponse("user_id", "USER_ID_NONE", "작성자 정보가 없습니다."));
        }

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            errors.add(new ErrorResponse("title", "TITLE_NONE", "제목이 비어있습니다."));
        } else if (request.getTitle().length() > 26) {
            errors.add(new ErrorResponse("title", "TITLE_TOO_LONG", "제목이 26자를 넘었습니다."));
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            errors.add(new ErrorResponse("content", "CONTENT_NONE", "내용이 비어있습니다."));
        }

        if (request.getCinema() == null) {
            errors.add(new ErrorResponse("cinema", "CINEMA_NONE", "상영관이 비어있습니다."));
        }

        if (request.getMovieName() == null || request.getMovieName().isBlank()) {
            errors.add(new ErrorResponse("movie_name", "MOVIE_NAME_NONE", "영화명이 비어있습니다."));
        }

        if (request.getPostType() == null) {
            errors.add(new ErrorResponse("post_type", "POST_TYPE_NONE", "게시글 유형이 비어있습니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("post_validation_error", null, errors);
        }

        try {
            CreatePostResponse data = postService.createPost(request, loginUserId);
            return new ApiResponse<>("post_create_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post", "POST_CREATE_FAIL", e.getMessage()));
            return new ApiResponse<>("post_create_fail", null, errors);
        }
    }

    @GetMapping
    public ApiResponse<List<PostListResponse>> getPostList() {
        List<PostListResponse> data = postService.postList();
        return new ApiResponse<>("post_list_success", data, null);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostDetail(@PathVariable Long postId, @AuthenticationPrincipal UserDetails loginUser) {
        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());

        try {
            PostDetailResponse data = postService.postDetail(loginUserId, postId);
            return new ApiResponse<>("post_detail_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post_id", "POST_DETAIL_FAIL", e.getMessage()));
            return new ApiResponse<>("post_detail_fail", null, errors);
        }
    }

    @PatchMapping("/{postId}")
    public ApiResponse<UpdatePostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequest request, @AuthenticationPrincipal UserDetails loginUser) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());

        if (request.getTitle() != null && request.getTitle().length() > 26) {
            errors.add(new ErrorResponse("title", "TITLE_TOO_LONG", "제목이 26자를 초과했습니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("post_validation_error", null, errors);
        }

        try {
            UpdatePostResponse data = postService.updatePost(postId, request, loginUserId);
            return new ApiResponse<>("post_edit_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post_id", "POST_EDIT_FAIL", e.getMessage()));
            return new ApiResponse<>("post_edit_fail", null, errors);
        }
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<DeletePostResponse> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails loginUser) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());
         DeletePostResponse data = postService.deletePost(postId, loginUserId);
            return new ApiResponse<>("post_delete_success", data, null);
    }
}
