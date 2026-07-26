package kr.adapterz.springboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequest {

    @NotBlank(message = "이메일이 비어있습니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank
    private String password;


    @Builder
    public LoginRequest(
            String email,
            String password
    ) {
        this.email = email;
        this.password = password;
    }
}