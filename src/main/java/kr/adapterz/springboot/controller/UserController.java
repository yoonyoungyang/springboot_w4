package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ApiResponse<SignUpResponse> signup(@RequestBody SignUpRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.add(new ErrorResponse("email", "EMAIL_NONE", "이메일이 비어있습니다."));
        } else if (!request.getEmail().contains("@")) {
            errors.add(new ErrorResponse("email", "EMAIL_FORM_ERROR", "이메일 형식이 아닙니다."));
        }

        String password = request.getPassword();

        if (password == null || password.isBlank()) {
            errors.add(new ErrorResponse("password", "PASSWORD_NONE", "비밀번호가 비어있습니다."));
        } else if (password.length() < 8) {
            errors.add(new ErrorResponse("password", "PASSWORD_TOO_SHORT", "비밀번호가 8자 미만입니다."));
        } else if (password.length() > 20) {
            errors.add(new ErrorResponse("password", "PASSWORD_TOO_LONG", "비밀번호가 20자 초과입니다."));
        } else if (!password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*\\d.*")
                || !password.matches(".*[!@#$%^&*(),.?':{}|<>].*")) {
            errors.add(new ErrorResponse(
                    "password",
                    "PASSWORD_NEED_SIGN",
                    "비밀번호에는 대문자, 소문자, 숫자, 특수문자를 최소 1개 이상씩 포함해야 합니다."
            ));
        }

        String nickname = request.getNickname();

        if (nickname == null || nickname.isBlank()) {
            errors.add(new ErrorResponse("nickname", "NICKNAME_NONE", "닉네임이 비어있습니다."));
        } else if (nickname.contains(" ")) {
            errors.add(new ErrorResponse("nickname", "NICKNAME_HAS_SPACE", "닉네임에 띄어쓰기가 존재합니다."));
        } else if (nickname.length() >= 11) {
            errors.add(new ErrorResponse("nickname", "NICKNAME_TOO_LONG", "닉네임이 11자 이상입니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("validation_error", null, errors);
        }

        if (userService.existsByEmail(request.getEmail())) {
            errors.add(new ErrorResponse("email", "EMAIL_DUPLICATION", "이메일이 중복입니다."));
        }

        if (userService.existsByNickname(nickname)) {
            errors.add(new ErrorResponse("nickname", "NICKNAME_DUPLICATION", "닉네임이 중복입니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("duplicate_error", null, errors);
        }

        SignUpResponse data = userService.signup(request);
        return new ApiResponse<>("signup_success", data, null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        List<ErrorResponse> errors = new ArrayList<>();

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            errors.add(new ErrorResponse("email", "EMAIL_NONE", "이메일이 비어있습니다."));
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            errors.add(new ErrorResponse("password", "PASSWORD_NONE", "비밀번호가 비어있습니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("login_error", null, errors);
        }

        try {
            LoginResponse data = userService.login(request);
            return new ApiResponse<>("login_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("login", "LOGIN_FAILED", "이메일 또는 비밀번호가 일치하지 않습니다."));
            return new ApiResponse<>("login_failed", null, errors);
        }
    }

    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getUser(@AuthenticationPrincipal UserDetails loginUser) {
        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());

        try {
            UserInfoResponse data = userService.getUser(loginUserId);
            return new ApiResponse<>("user_info_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("user", "USER_NOT_FOUND", e.getMessage()));
            return new ApiResponse<>("user_info_fail", null, errors);
        }
    }

    @PatchMapping("/me")
    public ApiResponse<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());

        if (request.getNickname() != null && request.getNickname().contains(" ")) {
            errors.add(new ErrorResponse("nickname", "NICKNAME_HAS_SPACE", "닉네임에 띄어쓰기가 존재합니다."));
        }

        if (request.getNickname() != null && request.getNickname().length() >= 11) {
            errors.add(new ErrorResponse("nickname", "NICKNAME_TOO_LONG", "닉네임이 11자 이상입니다."));
        }

        if (!errors.isEmpty()) {
            return new ApiResponse<>("user_validation_error", null, errors);
        }

        try {
            UpdateUserResponse data = userService.updateUser(request, loginUserId);
            return new ApiResponse<>("user_update_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("user", "USER_UPDATE_FAIL", e.getMessage()));
            return new ApiResponse<>("user_update_fail", null, errors);
        }
    }

    @PatchMapping("/me/password")
    public ApiResponse<UpdatePasswordResponse> updatePassword(@RequestBody UpdatePasswordRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());


        try {
            UpdatePasswordResponse data = userService.updatePassword(request, loginUserId);
            return new ApiResponse<>("password_update_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("password", "PASSWORD_UPDATE_FAIL", e.getMessage()));
            return new ApiResponse<>("password_update_fail", null, errors);
        }
    }

    @DeleteMapping("/me")
    public ApiResponse<DeleteUserResponse> deleteUser(@AuthenticationPrincipal UserDetails loginUser) {

        List<ErrorResponse> errors = new ArrayList<>();
        Long loginUserId = Long.valueOf(loginUser.getUsername());


        try {
            DeleteUserResponse data = userService.deleteUser(loginUserId);
            return new ApiResponse<>("user_delete_success", data, null);
        } catch (RuntimeException e) {
            errors.add(new ErrorResponse("user", "USER_DELETE_FAIL", e.getMessage()));
            return new ApiResponse<>("user_delete_fail", null, errors);
        }
    }
}