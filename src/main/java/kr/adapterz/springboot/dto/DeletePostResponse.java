package kr.adapterz.springboot.dto;

public class DeletePostResponse {

    private int post_id;

    public DeletePostResponse(int post_id) {
        this.post_id = post_id;
    }

    public int getPost_id() {
        return post_id;
    }
}