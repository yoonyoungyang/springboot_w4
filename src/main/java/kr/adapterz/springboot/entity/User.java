package kr.adapterz.springboot.entity;

import java.time.LocalDateTime;

public class User {
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
    private int userId;
    private LocalDateTime createdAt;

    public User(int userId, String email, String password, String nickname, String profileImg) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    public String getEmail () {
        return email;
    }

    public String getPassword () {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImg() {
        return profileImg;
    }
    public int getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
