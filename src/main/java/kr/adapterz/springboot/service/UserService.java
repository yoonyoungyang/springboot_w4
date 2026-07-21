package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.UserRepository;
import kr.adapterz.springboot.security.TokenProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
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
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .profileImg(request.getProfileImg())
                .build();


        User savedUser = userRepository.save(user);

        String token = tokenProvider.createToken(String.format("%s:%s", user.getUserId(),user.getRole()));

        return new SignUpResponse(savedUser, token);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("로그인 실패 - 사용자 정보 찾을 수 없음."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("로그인 실패 - 사용자 정보 찾을 수 없음.");
        }
        String token = tokenProvider.createToken(String.format("%s:%s", user.getUserId(), user.getRole()));

        return new LoginResponse(user, token);
    }

    public UserInfoResponse getUser(Long loginUserId) {
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
        return new UserInfoResponse(user);
    }

    @PreAuthorize("hasRole('USER')")
    public UpdateUserResponse updateUser(UpdateUserRequest request, Long loginUserId) {
        User user = userRepository.findById(loginUserId)
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

    @PreAuthorize("hasRole('USER')")
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request, Long loginUserId) {

        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new RuntimeException("비밀번호 변경 실패 - 회원 없음."));



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

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호 변경 실패 - 현재 비밀번호 일치하지 않음.");
        }

        user.updatePassword(request.getNewPassword());

        return new UpdatePasswordResponse(user);
    }

    @PreAuthorize("hasRole('USER')")
    public DeleteUserResponse deleteUser(Long loginUserId) {

        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new RuntimeException("회원 탈퇴 실패 - 회원 없음."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("회원 탈퇴 실패 - 비밀번호 다름.");
        }

        user.softDelete();

        return new DeleteUserResponse(user);
    }
}