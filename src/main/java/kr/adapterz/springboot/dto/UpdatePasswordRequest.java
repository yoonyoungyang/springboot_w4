package kr.adapterz.springboot.dto;

public class UpdatePasswordRequest {

    private int user_id;
    private String current_password;
    private String new_password;

    public int getUser_id() {
        return user_id;
    }

    public String getCurrent_password() {
        return current_password;
    }

    public String getNew_password() {
        return new_password;
    }
}
