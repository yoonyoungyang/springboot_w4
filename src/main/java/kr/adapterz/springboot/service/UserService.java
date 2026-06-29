package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }


    public SignUpResponse signup(SignUpRequest request) {


        if (userRepository.existsByNickname(request.getNickname())) {
            throw new RuntimeException("회원가입 실패 - 이미 사용 중인 닉네임");
        }

        int userId = userRepository.nextUserId();

        User user = new User(userId, request.getEmail(), request.getPassword(), request.getNickname(), request.getProfile_img());

        User savedUser = userRepository.save(user);

        SignUpResponse response = new SignUpResponse(savedUser.getUserId(), savedUser.getEmail(), savedUser.getNickname(), savedUser.getProfileImg(), savedUser.getCreatedAt());

        return response;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findUserByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("로그인 실패 - 사용자 정보 찾을 수 없음.");
        } else if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("로그인 실패 - 사용자 정보 찾을 수 없음.");
        } else {
            return new LoginResponse(user);
        }
    }

    public UpdateUserResponse updateUser(UpdateUserRequest request) {

        User user = userRepository.findUserById(request.getUser_id());

        if (user == null) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        if (request.getNickname() != null &&
                userRepository.existsByNickname(request.getNickname()) &&
                !user.getNickname().equals(request.getNickname())) {

            throw new RuntimeException("닉네임이 중복입니다.");
        }

        user.updateUser(
                request.getNickname(),
                request.getProfile_img()
        );

        return new UpdateUserResponse(user);
    }

    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {

        User user = userRepository.findUserById(request.getUser_id());

        if (user == null) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        if (!user.getPassword().equals(request.getCurrent_password())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        String password = request.getNew_password();

        if (password == null || password.isBlank()) {
            throw new RuntimeException("새 비밀번호가 비어있습니다.");
        }

        if (password.length() < 8) {
            throw new RuntimeException("비밀번호가 8자 미만입니다.");
        }

        if (password.length() > 20) {
            throw new RuntimeException("비밀번호가 20자를 초과했습니다.");
        }

        if (!password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*\\d.*")
                || !password.matches(".*[!@#$%^&*(),.?':{}|<>].*")) {

            throw new RuntimeException("비밀번호 형식이 올바르지 않습니다.");
        }

        user.updatePassword(password);

        return new UpdatePasswordResponse(user.getUserId());
    }

    public DeleteUserResponse deleteUser(int userId, String password) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new RuntimeException("회원이 존재하지 않습니다.");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.deleteUser(userId);

        return new DeleteUserResponse(userId);
    }
}
