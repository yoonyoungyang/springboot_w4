package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotBlank
    @NotBlank(message = "이메일이 비어있습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank
    private String password;
    
    @NotBlank
    private String nickname;

    @JsonProperty("profile_img")
    private String profileImg;
}