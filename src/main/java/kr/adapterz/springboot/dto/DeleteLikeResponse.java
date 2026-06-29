package kr.adapterz.springboot.dto;

public class DeleteLikeResponse {

    private int like_id;

    public DeleteLikeResponse(int like_id) {
        this.like_id = like_id;
    }

    public int getLike_id() {
        return like_id;
    }
}