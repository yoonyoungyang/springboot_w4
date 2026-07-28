package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin(origins = "http://127.0.0.1:5500")

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping
    public ApiResponse<List<CommentListResponse>> commentList(
            @PathVariable Long postId, @AuthenticationPrincipal UserDetails loginUser) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());



        try {
            List<CommentListResponse> data = commentService.commentList(postId, loginUserId);
            return new ApiResponse<>("comment_list_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse(
                    "post_id",
                    "COMMENT_LIST_FAIL",
                    e.getMessage()
            ));

            return new ApiResponse<>("comment_list_fail", null, errors);
        }
    }

    

    @PostMapping
    public ApiResponse<CreateCommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request, @AuthenticationPrincipal UserDetails loginUser ) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());


        if (request.getContent() == null || request.getContent().isBlank()) {
            errors.add(new ErrorResponse("content", "CONTENT_NONE", "댓글 내용이 비어있습니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("comment_validation_error", null, errors);
        }

        try {
            CreateCommentResponse data = commentService.createComment(postId, request, loginUserId);
            return new ApiResponse<>("comment_create_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post_id", "COMMENT_CREATE_FAIL", e.getMessage()));
            return new ApiResponse<>("comment_create_fail", null, errors);
        }
    }

    @PatchMapping("/{commentId}")
    public ApiResponse<UpdateCommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request, @AuthenticationPrincipal UserDetails loginUser) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());


        if (request.getContent() == null || request.getContent().isBlank()) {
            errors.add(new ErrorResponse("content", "CONTENT_NONE", "댓글 내용이 비어있습니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("comment_validation_error", null, errors);
        }

        try {
            UpdateCommentResponse data = commentService.updateComment(commentId, request, loginUserId);
            return new ApiResponse<>("comment_edit_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("comment_id", "COMMENT_EDIT_FAIL", e.getMessage()));
            return new ApiResponse<>("comment_edit_fail", null, errors);
        }
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<DeleteCommentResponse> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails loginUser) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());


        try {
            DeleteCommentResponse data = commentService.deleteComment(commentId, loginUserId);
            return new ApiResponse<>("comment_delete_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("comment_id", "COMMENT_DELETE_FAIL", e.getMessage()));
            return new ApiResponse<>("comment_delete_fail", null, errors);
        }
    }
}