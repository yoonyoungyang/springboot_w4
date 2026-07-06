package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeleteUserResponse {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    public DeleteUserResponse(User user) {
        this.userId = user.getUserId();
        this.deletedAt = user.getDeletedAt();
    }
}