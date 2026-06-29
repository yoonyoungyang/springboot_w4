package kr.adapterz.springboot.dto;

public class UpdateUserRequest {

    private int user_id;
    private String nickname;
    private String profile_img;

    public int getUser_id() {
        return user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfile_img() {
        return profile_img;
    }
}
