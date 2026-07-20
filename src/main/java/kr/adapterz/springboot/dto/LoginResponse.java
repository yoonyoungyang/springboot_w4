package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.User;
import lombok.Getter;

@Getter
public class LoginResponse {

    @JsonProperty("user_id")
    private Long userId;

    private String nickname;

    private String email;

    @JsonProperty("profile_img")
    private String profileImg;

    private String token;

    public LoginResponse(User user, String token) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImg = user.getProfileImg();
        this.token = token;
    }
}