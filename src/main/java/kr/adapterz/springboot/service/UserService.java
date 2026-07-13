package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public SignUpResponse signup(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("회원가입 실패 - 이미 사용 중인 이메일");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new RuntimeException("회원가입 실패 - 이미 사용 중인 닉네임");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .profileImg(request.getProfileImg())
                .build();

        User savedUser = userRepository.save(user);

        return new SignUpResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("로그인 실패 - 사용자 정보 찾을 수 없음."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("로그인 실패 - 사용자 정보 찾을 수 없음.");
        }

        return new LoginResponse(user);
    }

    public UpdateUserResponse updateUser(UpdateUserRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("회원 정보 수정 실패 - 회원 없음."));

        if (request.getNickname() != null
                && userRepository.existsByNickname(request.getNickname())
                && !user.getNickname().equals(request.getNickname())) {
            throw new RuntimeException("회원 정보 수정 실패 - 닉네임 중복.");
        }

        user.updateUser(
                request.getNickname(),
                request.getProfileImg()
        );

        return new UpdateUserResponse(user);
    }
    public UserInfoResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        return new UserInfoResponse(user);
    }

    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("비밀번호 변경 실패 - 회원 없음."));

        if (!user.getPassword().equals(request.getCurrentPassword())) {
            throw new RuntimeException("비밀번호 변경 실패 - 현재 비밀번호 일치하지 않음.");
        }

        String password = request.getNewPassword();

        if (password == null || password.isBlank()) {
            throw new RuntimeException("비밀번호 변경 실패 - 새 비밀번호가 비어있음.");
        }

        if (password.length() < 8) {
            throw new RuntimeException("비밀번호 변경 실패 - 비밀번호가 8자 미만.");
        }

        if (password.length() > 20) {
            throw new RuntimeException("비밀번호 변경 실패 - 비밀번호가 20자 초과.");
        }

        if (!password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*\\d.*")
                || !password.matches(".*[!@#$%^&*(),.?':{}|<>].*")) {
            throw new RuntimeException("비밀번호 변경 실패 - 비밀번호 형식이 올바르지 않음.");
        }

        user.updatePassword(password);

        return new UpdatePasswordResponse(user);
    }

    public DeleteUserResponse deleteUser(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("회원 탈퇴 실패 - 회원 없음."));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("회원 탈퇴 실패 - 비밀번호 다름.");
        }

        user.softDelete();

        return new DeleteUserResponse(user);
    }
}