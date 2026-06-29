package kr.adapterz.springboot.dto;

public class CreateCommentRequest {

    private int user_id;
    private String content;

    public int getUser_id() {
        return user_id;
    }

    public String getContent() {
        return content;
    }
}