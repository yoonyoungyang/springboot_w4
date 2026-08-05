package kr.adapterz.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.adapterz.springboot.controller.UserController;
import kr.adapterz.springboot.dto.DeleteUserResponse;
import kr.adapterz.springboot.dto.LoginRequest;
import kr.adapterz.springboot.dto.LoginResponse;
import kr.adapterz.springboot.dto.SignUpRequest;
import kr.adapterz.springboot.dto.SignUpResponse;
import kr.adapterz.springboot.dto.UpdatePasswordRequest;
import kr.adapterz.springboot.dto.UpdatePasswordResponse;
import kr.adapterz.springboot.dto.UpdateUserRequest;
import kr.adapterz.springboot.dto.UpdateUserResponse;
import kr.adapterz.springboot.dto.UserInfoResponse;
import kr.adapterz.springboot.security.TokenProvider;
import kr.adapterz.springboot.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean(name = "jpaMappingContext")
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpSuccess() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        SignUpResponse response = mock(SignUpResponse.class);

        given(userService.existsByEmail(request.getEmail()))
                .willReturn(false);
        given(userService.existsByNickname(request.getNickname()))
                .willReturn(false);
        given(response.getEmail()).willReturn(request.getEmail());
        given(response.getNickname()).willReturn(request.getNickname());
        given(response.getProfileImg()).willReturn(request.getProfileImg());
        given(userService.signup(any(SignUpRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("signup_success"))
                .andExpect(jsonPath("$.data.email")
                        .value(request.getEmail()))
                .andExpect(jsonPath("$.data.nickname")
                        .value(request.getNickname()))
                .andExpect(jsonPath("$.data.profile_img")
                        .value(request.getProfileImg()))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(userService).existsByEmail(request.getEmail());
        verify(userService).existsByNickname(request.getNickname());
        verify(userService).signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 비어 있음")
    void signUpFail_ifEmailBlank() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("validation_error"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("email"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("EMAIL_NONE"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("이메일이 비어있습니다."));

        verify(userService, never())
                .signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 형식 오류")
    void signUpFail_ifEmailFormatInvalid() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("invalid-email")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("validation_error"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("email"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("EMAIL_FORM_ERROR"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("이메일 형식이 아닙니다."));

        verify(userService, never())
                .signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 비밀번호 형식 오류")
    void signUpFail_ifPasswordInvalid() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("1234")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("validation_error"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("password"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("PASSWORD_TOO_SHORT"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("비밀번호가 8자 미만입니다."));

        verify(userService, never())
                .signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 이메일 중복 테스트")
    void signUpFail_ifExistsEmail() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        given(userService.existsByEmail(request.getEmail()))
                .willReturn(true);
        given(userService.existsByNickname(request.getNickname()))
                .willReturn(false);

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("duplicate_error"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("email"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("EMAIL_DUPLICATION"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("이메일이 중복입니다."));

        verify(userService).existsByEmail(request.getEmail());
        verify(userService).existsByNickname(request.getNickname());
        verify(userService, never())
                .signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 닉네임 중복 테스트")
    void signUpFail_ifExistsNickname() throws Exception {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        given(userService.existsByEmail(request.getEmail()))
                .willReturn(false);
        given(userService.existsByNickname(request.getNickname()))
                .willReturn(true);

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("duplicate_error"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("nickname"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("NICKNAME_DUPLICATION"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("닉네임이 중복입니다."));

        verify(userService).existsByEmail(request.getEmail());
        verify(userService).existsByNickname(request.getNickname());
        verify(userService, never())
                .signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .build();

        LoginResponse response = mock(LoginResponse.class);

        given(response.getEmail()).willReturn(request.getEmail());
        given(response.getToken()).willReturn("access-token");
        given(userService.login(any(LoginRequest.class)))
                .willReturn(response);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("login_success"))
                .andExpect(jsonPath("$.data.email")
                        .value(request.getEmail()))
                .andExpect(jsonPath("$.data.token")
                        .value("access-token"))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 이메일 비어 있음")
    void loginFail_ifEmailBlank() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("")
                .password("Password1!")
                .build();

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("login_error"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("email"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("EMAIL_NONE"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("이메일이 비어있습니다."));

        verify(userService, never())
                .login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 사용자 정보 없음")
    void loginFail_ifUserNotFound() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .build();

        given(userService.login(any(LoginRequest.class)))
                .willThrow(new RuntimeException(
                        "로그인 실패 - 사용자 정보 찾을 수 없음."
                ));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("login_failed"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("login"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("LOGIN_FAILED"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("이메일 또는 비밀번호가 일치하지 않습니다."));

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 검사")
    void loginFail_ifNotMatchesPassword() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("WrongPassword1!")
                .build();

        given(userService.login(any(LoginRequest.class)))
                .willThrow(new RuntimeException("비밀번호가 일치하지 않습니다."));

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("login_failed"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("login"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("LOGIN_FAILED"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("이메일 또는 비밀번호가 일치하지 않습니다."));

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("회원 정보 조회 성공 테스트")
    @WithMockUser(username = "1", roles = "USER")
    void getUserSuccess() throws Exception {
        UserInfoResponse response = mock(UserInfoResponse.class);

        given(response.getEmail()).willReturn("test@email.com");
        given(response.getNickname()).willReturn("테스트닉네임");
        given(response.getProfileImg()).willReturn("profile.png");
        given(userService.getUser(1L)).willReturn(response);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("user_info_success"))
                .andExpect(jsonPath("$.data.email")
                        .value("test@email.com"))
                .andExpect(jsonPath("$.data.nickname")
                        .value("테스트닉네임"))
                .andExpect(jsonPath("$.data.profile_img")
                        .value("profile.png"))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(userService).getUser(1L);
    }

    @Test
    @DisplayName("회원 정보 조회 실패 테스트 - 회원 없음")
    @WithMockUser(username = "1", roles = "USER")
    void getUserFail_ifUserNotFound() throws Exception {
        given(userService.getUser(1L))
                .willThrow(new RuntimeException(
                        "존재하지 않는 사용자입니다."
                ));

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("user_info_fail"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("user"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("USER_NOT_FOUND"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("존재하지 않는 사용자입니다."));

        verify(userService).getUser(1L);
    }

    @Test
    @DisplayName("회원 정보 수정 성공 테스트")
    @WithMockUser(username = "1", roles = "USER")
    void updateUserSuccess() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname("수정닉네임")
                .profileImg("new-profile.png")
                .build();

        UpdateUserResponse response = mock(UpdateUserResponse.class);

        given(response.getNickname()).willReturn(request.getNickname());
        given(response.getProfileImg()).willReturn(request.getProfileImg());
        given(userService.updateUser(
                any(UpdateUserRequest.class),
                any(Long.class)
        )).willReturn(response);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("user_update_success"))
                .andExpect(jsonPath("$.data.nickname")
                        .value(request.getNickname()))
                .andExpect(jsonPath("$.data.profile_img")
                        .value(request.getProfileImg()))
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(userService).updateUser(
                any(UpdateUserRequest.class),
                any(Long.class)
        );
    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 변경 사항 없음")
    @WithMockUser(username = "1", roles = "USER")
    void updateUserFail_ifNotExistsDiff() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname(null)
                .profileImg(null)
                .build();

        given(userService.updateUser(
                any(UpdateUserRequest.class),
                any(Long.class)
        )).willThrow(new RuntimeException("변경 사항이 없습니다."));

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("user_update_fail"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("user"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("USER_UPDATE_FAIL"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("변경 사항이 없습니다."));

        verify(userService).updateUser(
                any(UpdateUserRequest.class),
                any(Long.class)
        );
    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 닉네임 비어 있음")
    @WithMockUser(username = "1", roles = "USER")
    void updateUserFail_ifNicknameBlank() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname("  ")
                .profileImg("profile.png")
                .build();

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("user_validation_error"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("nickname"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("NICKNAME_HAS_SPACE"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("닉네임에 띄어쓰기가 존재합니다."));

        verify(userService, never()).updateUser(
                any(UpdateUserRequest.class),
                any(Long.class)
        );
    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 닉네임 중복")
    @WithMockUser(username = "1", roles = "USER")
    void updateUserFail_ifExistsNickname() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname("중복닉네임")
                .profileImg("profile.png")
                .build();

        given(userService.updateUser(
                any(UpdateUserRequest.class),
                any(Long.class)
        )).willThrow(new RuntimeException(
                "회원 정보 수정 실패 - 닉네임 중복."
        ));

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("user_update_fail"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("user"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("USER_UPDATE_FAIL"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("회원 정보 수정 실패 - 닉네임 중복."));

        verify(userService).updateUser(
                any(UpdateUserRequest.class),
                any(Long.class)
        );
    }

    @Test
    @DisplayName("비밀번호 수정 성공 테스트")
    @WithMockUser(username = "1", roles = "USER")
    void updatePasswordSuccess() throws Exception {
        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword("Password1!")
                .newPassword("NewPassword1!")
                .build();

        UpdatePasswordResponse response =
                mock(UpdatePasswordResponse.class);

        given(userService.updatePassword(
                any(UpdatePasswordRequest.class),
                any(Long.class)
        )).willReturn(response);

        mockMvc.perform(patch("/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("password_update_success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.errors").isEmpty());

        verify(userService).updatePassword(
                any(UpdatePasswordRequest.class),
                any(Long.class)
        );
    }

    @Test
    @DisplayName("비밀번호 수정 실패 테스트 - 현재 비밀번호 비어 있음")
    @WithMockUser(username = "1", roles = "USER")
    void updatePasswordFail_ifCurrentPasswordBlank() throws Exception {
        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword("")
                .newPassword("NewPassword1!")
                .build();

        given(userService.updatePassword(
                any(UpdatePasswordRequest.class),
                any(Long.class)
        )).willThrow(new RuntimeException(
                "현재 비밀번호가 비어있습니다."
        ));

        mockMvc.perform(patch("/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("password_update_fail"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("password"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("PASSWORD_UPDATE_FAIL"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("현재 비밀번호가 비어있습니다."));

        verify(userService).updatePassword(
                any(UpdatePasswordRequest.class),
                any(Long.class)
        );
    }

    @Test
    @DisplayName("비밀번호 수정 실패 테스트 - 새 비밀번호 형식 오류")
    @WithMockUser(username = "1", roles = "USER")
    void updatePasswordFail_ifNewPasswordInvalid() throws Exception {
        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword("Password1!")
                .newPassword("1234")
                .build();

        given(userService.updatePassword(
                any(UpdatePasswordRequest.class),
                any(Long.class)
        )).willThrow(new RuntimeException(
                "새 비밀번호 형식이 올바르지 않습니다."
        ));

        mockMvc.perform(patch("/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("password_update_fail"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("password"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("PASSWORD_UPDATE_FAIL"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("새 비밀번호 형식이 올바르지 않습니다."));

        verify(userService).updatePassword(
                any(UpdatePasswordRequest.class),
                any(Long.class)
        );
    }

    @Test
    @DisplayName("비밀번호 수정 실패 테스트 - 회원 없음")
    @WithMockUser(username = "1", roles = "USER")
    void updatePasswordFail_ifUserNotFound() throws Exception {
        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword("Password1!")
                .newPassword("NewPassword1!")
                .build();

        given(userService.updatePassword(
                any(UpdatePasswordRequest.class),
                any(Long.class)
        )).willThrow(new RuntimeException(
                "비밀번호 수정 실패 - 회원 없음."
        ));

        mockMvc.perform(patch("/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("password_update_fail"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("password"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("PASSWORD_UPDATE_FAIL"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("비밀번호 수정 실패 - 회원 없음."));

        verify(userService).updatePassword(
                any(UpdatePasswordRequest.class),
                any(Long.class)
        );
    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    @WithMockUser(username = "1", roles = "USER")
    void deleteUserSuccess() throws Exception {
        DeleteUserResponse response = mock(DeleteUserResponse.class);

        given(userService.deleteUser(1L))
                .willReturn(response);

        mockMvc.perform(delete("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("user_delete_success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.errors").isEmpty());


        verify(userService).deleteUser(1L);

    }

    @Test
    @DisplayName("회원 탈퇴 실패 테스트 - 회원 없음")
    @WithMockUser(username = "1", roles = "USER")
    void deleteUserFail_ifUserNotFound() throws Exception {
        given(userService.deleteUser(1L))
                .willThrow(new RuntimeException(
                        "회원 탈퇴 실패 - 회원 없음."
                ));

        mockMvc.perform(delete("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("user_delete_fail"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.errors[0].field")
                        .value("user"))
                .andExpect(jsonPath("$.errors[0].code")
                        .value("USER_DELETE_FAIL"))
                .andExpect(jsonPath("$.errors[0].error_message")
                        .value("회원 탈퇴 실패 - 회원 없음."));

        verify(userService).deleteUser(1L);
    }
}