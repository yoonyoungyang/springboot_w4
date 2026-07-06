package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    private String nickname;

    @JsonProperty("profile_img")
    private String profileImg;
}