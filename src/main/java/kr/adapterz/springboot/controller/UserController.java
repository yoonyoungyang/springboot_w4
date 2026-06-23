package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.common.ApiResponse;
import kr.adapterz.springboot.dto.ErrorResponse;
import kr.adapterz.springboot.dto.SignUpRequest;
import kr.adapterz.springboot.dto.SignUpResponse;
import kr.adapterz.springboot.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController // HTTP 요청을 받는 클래스 -> 컨트롤러 등록
@RequestMapping("/users") //공통 경로
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

        if (password== null || password.isBlank()) {
            errors.add(new ErrorResponse("password", "PASSWORD_NONE", "비밀번호가 비어있습니다."));
        } else if (password.length() < 8) {
            errors.add(new ErrorResponse("password", "PASSWORD_TOO_SHORT", "비밀번호가 8자 미만입니다."));
        } else if (password.length() > 20) {
            errors.add(new ErrorResponse("password", "PASSWORD_TOO_LONG", "비밀번호가 20자 초과입니다."));
        } else if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*(),.?':{}|<>].*")) {
            errors.add(new ErrorResponse("password", "PASSWORD_NEED_SIGN", "비밀번호에는 대문자,소문자,숫자,특수문자를 최소 1개 이상씩 포함해야 합니다."));
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
}