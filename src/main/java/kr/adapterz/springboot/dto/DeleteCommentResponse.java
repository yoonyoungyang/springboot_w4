package kr.adapterz.springboot.dto;

public class DeleteCommentResponse {

    private int comment_id;

    public DeleteCommentResponse(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getComment_id() {
        return comment_id;
    }
}