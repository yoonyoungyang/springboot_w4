package kr.adapterz.springboot.dto;

public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
    private String profile_img;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getNickname() {
        return nickname;
    }

    public String getProfile_img() {
        return profile_img;
    }
}
