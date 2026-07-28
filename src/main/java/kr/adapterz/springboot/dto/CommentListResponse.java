package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentListResponse {
    private List<CommentResponse> comments;

    @JsonProperty("total_count")
    private Long totalCount;

    public CommentListResponse (List<CommentResponse> comments, Long totalCount) {
        this.comments = comments;
        this.totalCount = totalCount;
    }
}
