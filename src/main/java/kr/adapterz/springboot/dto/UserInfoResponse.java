package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.User;
import lombok.Getter;

@Getter
public class UserInfoResponse {

    @JsonProperty("user_id")
    private Long userId;

    private String nickname;

    private String email;

    @JsonProperty("profile_img")
    private String profileImg;

    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImg = user.getProfileImg();
    }
}