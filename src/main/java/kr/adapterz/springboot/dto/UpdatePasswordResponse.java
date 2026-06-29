package kr.adapterz.springboot.dto;

public class UpdatePasswordResponse {

    private int user_id;

    public UpdatePasswordResponse(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }
}