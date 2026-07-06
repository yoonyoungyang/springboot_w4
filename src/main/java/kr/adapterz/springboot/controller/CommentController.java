package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ApiResponse<CreateCommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request) {

        List<ErrorResponse> errors = new ArrayList<>();

        if (request.getContent() == null || request.getContent().isBlank()) {
            errors.add(new ErrorResponse("content", "CONTENT_NONE", "댓글 내용이 비어있습니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("comment_validation_error", null, errors);
        }

        try {
            CreateCommentResponse data = commentService.createComment(postId, request);
            return new ApiResponse<>("comment_create_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post_id", "COMMENT_CREATE_FAIL", e.getMessage()));
            return new ApiResponse<>("comment_create_fail", null, errors);
        }
    }

    @PatchMapping("/{commentId}")
    public ApiResponse<UpdateCommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request) {

        List<ErrorResponse> errors = new ArrayList<>();

        if (request.getContent() == null || request.getContent().isBlank()) {
            errors.add(new ErrorResponse("content", "CONTENT_NONE", "댓글 내용이 비어있습니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("comment_validation_error", null, errors);
        }

        try {
            UpdateCommentResponse data = commentService.updateComment(commentId, request);
            return new ApiResponse<>("comment_edit_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("comment_id", "COMMENT_EDIT_FAIL", e.getMessage()));
            return new ApiResponse<>("comment_edit_fail", null, errors);
        }
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<DeleteCommentResponse> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) {

        List<ErrorResponse> errors = new ArrayList<>();

        try {
            DeleteCommentResponse data = commentService.deleteComment(commentId, userId);
            return new ApiResponse<>("comment_delete_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("comment_id", "COMMENT_DELETE_FAIL", e.getMessage()));
            return new ApiResponse<>("comment_delete_fail", null, errors);
        }
    }
}