package kr.adapterz.springboot.dto;

import java.time.LocalDateTime;

public class SignUpResponse {
    private int user_id;
    private String email;
    private String nickname;
    private String profile_img;
    private LocalDateTime created_at;

    public SignUpResponse(int user_id, String email, String nickname, String profile_img, LocalDateTime created_at) {
        this.user_id = user_id;
        this.email = email;
        this.nickname = nickname;
        this.profile_img = profile_img;
        this.created_at = created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getEmail () {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
}
