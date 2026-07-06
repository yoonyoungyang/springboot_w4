package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignUpResponse {

    @JsonProperty("user_id")
    private Long userId;

    private String email;
    private String nickname;

    @JsonProperty("profile_img")
    private String profileImg;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public SignUpResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.createdAt = user.getCreatedAt();
    }
}