package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ApiResponse<CreateLikeResponse> createLike(
            @PathVariable Long postId,
            @RequestBody CreateLikeRequest request) {

        List<ErrorResponse> errors = new ArrayList<>();

        try {
            CreateLikeResponse data = likeService.createLike(postId, request);
            return new ApiResponse<>("like_create_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post_id", "LIKE_CREATE_FAIL", e.getMessage()));
            return new ApiResponse<>("like_create_fail", null, errors);
        }
    }

    @DeleteMapping
    public ApiResponse<DeleteLikeResponse> deleteLike(
            @PathVariable Long postId,
            @RequestParam Long userId) {

        List<ErrorResponse> errors = new ArrayList<>();

        try {
            DeleteLikeResponse data = likeService.deleteLike(postId, userId);
            return new ApiResponse<>("like_delete_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post_id", "LIKE_DELETE_FAIL", e.getMessage()));
            return new ApiResponse<>("like_delete_fail", null, errors);
        }
    }
}