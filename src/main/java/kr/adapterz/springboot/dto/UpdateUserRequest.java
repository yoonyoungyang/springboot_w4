package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    @NotNull
    private String nickname;

    @JsonProperty("profile_img")
    private String profileImg;
}