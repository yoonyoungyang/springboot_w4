package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.service.LikeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")

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
            @RequestBody CreateLikeRequest request, @AuthenticationPrincipal UserDetails loginUser) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());

        try {
            CreateLikeResponse data = likeService.createLike(postId, request, loginUserId);
            return new ApiResponse<>("like_create_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post_id", "LIKE_CREATE_FAIL", e.getMessage()));
            return new ApiResponse<>("like_create_fail", null, errors);
        }
    }

    @DeleteMapping
    public ApiResponse<DeleteLikeResponse> deleteLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails loginUser) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());


        try {
            DeleteLikeResponse data = likeService.deleteLike(postId, loginUserId);
            return new ApiResponse<>("like_delete_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("post_id", "LIKE_DELETE_FAIL", e.getMessage()));
            return new ApiResponse<>("like_delete_fail", null, errors);
        }
    }
}