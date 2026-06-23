package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.ErrorResponse;
import kr.adapterz.springboot.dto.SignUpRequest;
import kr.adapterz.springboot.dto.SignUpResponse;
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
            throw new RuntimeException("이미 사용 중인 닉네임입니다.");
        }

        int userId = userRepository.nextUserId();

        User user = new User(userId, request.getEmail(), request.getPassword(), request.getNickname(), request.getProfile_img());

        User savedUser = userRepository.save(user);

        SignUpResponse response = new SignUpResponse(savedUser.getUserId(), savedUser.getEmail(), savedUser.getNickname(), savedUser.getProfileImg(), savedUser.getCreatedAt());

        return response;
    }
}
