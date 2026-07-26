package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateUserRequest {

    @NotNull
    private String nickname;

    @JsonProperty("profile_img")
    private String profileImg;

    @Builder
    public UpdateUserRequest(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;

    }
}