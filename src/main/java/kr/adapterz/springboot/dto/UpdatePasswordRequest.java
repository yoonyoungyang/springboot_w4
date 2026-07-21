package kr.adapterz.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdatePasswordRequest {
    

    @NotBlank(message = "이전 비밀번호가 비어있습니다.")
    @JsonProperty("current_password")
    private String currentPassword;

    @NotBlank(message = "바꿀 비밀번호가 비어있습니다.")
    @JsonProperty("new_password")
    private String newPassword;
}