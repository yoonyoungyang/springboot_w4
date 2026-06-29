package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.service.PostService;
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
    public ApiResponse<CreatePostResponse> createPost(@RequestBody CreatePostRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();
        if (request.getUser_id() <= 0) {
            errors.add(new ErrorResponse("user_id", "USER_ID_NONE", "작성자 정보가 없습니다."));
        }

        if (request.getTitle() ==null || request.getTitle().isBlank() ) {
            errors.add(new ErrorResponse("title", "TITLE_NONE", "제목이 비어있습니다."));
        } else if (request.getTitle().length() > 26) {
            errors.add(new ErrorResponse("title", "TITLE_TOO_LONG", "제목이 26자를 넘었습니다."));
        }
        if (request.getContent() ==null || request.getContent().isBlank()) {
            errors.add(new ErrorResponse("content", "CONTENT_NONE", "내용이 비어있습니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("post_validation_error", null, errors);
        }
        CreatePostResponse data = postService.createPost(request);
        return new ApiResponse<>("post_create_success", data, null);
    }

    @GetMapping
    public ApiResponse<List<PostListResponse>> getPostList() {
        List<PostListResponse> data = postService.postList();
        return new ApiResponse<>("post_list_success", data, null);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostDetail(@PathVariable int postId) {
        List<ErrorResponse> errors = new ArrayList<>();

        try {
            PostDetailResponse data = postService.postDetail(postId);
            return new ApiResponse<>("post_detail_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse(null, "POST_DETAIL_FAIL", "게시글을 찾을 수 없습니다."));
            return new ApiResponse<>("post_detail_fail", null, errors );
        }
    }
    @PatchMapping("/{postId}")
    public ApiResponse<UpdatePostResponse> updatePost(
            @PathVariable int postId,
            @RequestBody UpdatePostRequest request) {

        List<ErrorResponse> errors = new ArrayList<>();

        if (request.getTitle() != null &&
                request.getTitle().length() > 26) {

            errors.add(new ErrorResponse(
                    "title",
                    "TITLE_TOO_LONG",
                    "제목이 26자를 초과했습니다."
            ));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("post_validation_error", null, errors);
        }

        try {

            UpdatePostResponse data =
                    postService.updatePost(postId, request);

            return new ApiResponse<>("post_edit_success", data, null);

        } catch (RuntimeException e) {

            errors.add(new ErrorResponse(
                    "post_id",
                    "POST_EDIT_FAIL",
                    e.getMessage()
            ));

            return new ApiResponse<>("post_edit_fail", null, errors);
        }
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<DeletePostResponse> deletePost(
            @PathVariable int postId,
            @RequestParam int userId) {

        List<ErrorResponse> errors = new ArrayList<>();

        try {

            DeletePostResponse data =
                    postService.deletePost(postId, userId);

            return new ApiResponse<>("post_delete_success", data, null);

        } catch (RuntimeException e) {

            errors.add(new ErrorResponse(
                    "post_id",
                    "POST_DELETE_FAIL",
                    e.getMessage()
            ));

            return new ApiResponse<>("post_delete_fail", null, errors);
        }
    }



}
