package kr.adapterz.springboot.dto;

public class UpdatePostRequest {
    private int user_id;
    private String title;
    private String content;
    private String content_img;


    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getContent_img() {
        return content_img;
    }
}
