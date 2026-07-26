package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignUpRequest {

    @NotBlank(message = "이메일이 비어있습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호가 비어 있습니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    private String password;

    @NotBlank
    private String nickname;

    @JsonProperty("profile_img")
    private String profileImg;

    @Builder
    public SignUpRequest(
            String email,
            String password,
            String nickname,
            String profileImg
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }


}