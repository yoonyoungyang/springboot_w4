package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.adapterz.springboot.entity.User;
import lombok.Getter;

@Getter
public class UpdatePasswordResponse {

    @JsonProperty("user_id")
    private Long userId;

    public UpdatePasswordResponse(User user) {
        this.userId = user.getUserId();
    }
}