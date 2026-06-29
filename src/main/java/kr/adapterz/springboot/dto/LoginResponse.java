package kr.adapterz.springboot.dto;

import kr.adapterz.springboot.entity.User;

public class LoginResponse {
    private int user_id;
    private String nickname;
    private String email;
    private String profile_img;


    public LoginResponse (User user) {
        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profile_img = user.getProfileImg();
    }

    public int getUser_id() {
        return user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
    public String getProfile_img() {
        return profile_img;
    }


}
