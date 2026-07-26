package kr.adapterz.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.adapterz.springboot.controller.UserController;
import kr.adapterz.springboot.dto.LoginRequest;
import kr.adapterz.springboot.dto.LoginResponse;
import kr.adapterz.springboot.dto.SignUpRequest;
import kr.adapterz.springboot.dto.SignUpResponse;
import kr.adapterz.springboot.dto.UpdatePasswordRequest;
import kr.adapterz.springboot.dto.UpdatePasswordResponse;
import kr.adapterz.springboot.dto.UpdateUserRequest;
import kr.adapterz.springboot.dto.UpdateUserResponse;
import kr.adapterz.springboot.dto.UserInfoResponse;
import kr.adapterz.springboot.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpSuccess() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        SignUpResponse response = mock(SignUpResponse.class);
        given(response.getEmail()).willReturn(request.getEmail());
        given(response.getNickname()).willReturn(request.getNickname());
        given(response.getProfileImg()).willReturn(request.getProfileImg());
        given(userService.signup(any(SignUpRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email")
                        .value(request.getEmail()))
                .andExpect(jsonPath("$.data.nickname")
                        .value(request.getNickname()))
                .andExpect(jsonPath("$.data.profile_img")
                        .value(request.getProfileImg()));

        verify(userService).signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 비어 있음")
    void signUpFail_ifEmailBlank() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        // when & then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 이메일 형식 오류")
    void signUpFail_ifEmailFormatInvalid() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("invalid-email")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        // when & then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 비밀번호 형식 오류")
    void signUpFail_ifPasswordInvalid() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("1234")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        // when & then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 이메일 중복 테스트")
    void signUpFail_ifExistsEmail() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        given(userService.signup(any(SignUpRequest.class))).willThrow(new RuntimeException("회원가입 실패 - 이미 사용 중인 이메일"));

        // when & then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService).signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("회원가입 닉네임 중복 테스트")
    void signUpFail_ifExistsNickname() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        given(userService.signup(any(SignUpRequest.class)))
                .willThrow(new RuntimeException("회원가입 실패 - 이미 사용 중인 닉네임"));

        // when & then
        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService).signup(any(SignUpRequest.class));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .build();

        LoginResponse response = mock(LoginResponse.class);

        given(response.getEmail()).willReturn(request.getEmail());
        given(response.getToken()).willReturn("access-token");
        given(userService.login(any(LoginRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email")
                        .value(request.getEmail()))
                .andExpect(jsonPath("$.data.token")
                        .value("access-token"));

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 이메일 비어 있음")
    void loginFail_ifEmailBlank() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("")
                .password("Password1!")
                .build();

        // when & then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 사용자 정보 없음")
    void loginFail_ifUserNotFound() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .build();

        given(userService.login(any(LoginRequest.class))).willThrow(new RuntimeException("로그인 실패 - 사용자 정보 찾을 수 없음."));

        // when & then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 검사")
    void loginFail_ifNotMatchesPassword() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("WrongPassword1!")
                .build();

        given(userService.login(any(LoginRequest.class))).willThrow(new RuntimeException("로그인 실패 - 사용자 정보 찾을 수 없음."));

        // when & then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("회원 정보 조회 성공 테스트")
    @WithMockUser(username = "1", roles = "USER")
    void getUserSuccess() throws Exception {
        // given
        UserInfoResponse response = mock(UserInfoResponse.class);
        given(response.getEmail()).willReturn("test@email.com");
        given(response.getNickname()).willReturn("테스트닉네임");
        given(response.getProfileImg()).willReturn("profile.png");
        given(userService.getUser(1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email")
                        .value("test@email.com"))
                .andExpect(jsonPath("$.data.nickname")
                        .value("테스트닉네임"))
                .andExpect(jsonPath("$.data.profile_img")
                        .value("profile.png"));

        verify(userService).getUser(1L);
    }

    @Test
    @DisplayName("회원 정보 조회 실패 테스트 - 회원 없음")
    @WithMockUser(username = "1", roles = "USER")
    void getUserFail_ifUserNotFound() throws Exception {
        // given
        given(userService.getUser(1L)).willThrow(new RuntimeException("존재하지 않는 사용자입니다."));

        // when & then
        mockMvc.perform(get("/users/me")).andExpect(status().isBadRequest());
        verify(userService).getUser(1L);
    }

    @Test
    @DisplayName("회원 정보 수정 성공 테스트")
    @WithMockUser(username = "1", roles = "USER")
    void updateUserSuccess() throws Exception {
        // given
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname("수정닉네임")
                .profileImg("new-profile.png")
                .build();

        UpdateUserResponse response = mock(UpdateUserResponse.class);
        given(response.getNickname()).willReturn(request.getNickname());
        given(response.getProfileImg()).willReturn(request.getProfileImg());
        given(userService.updateUser(any(UpdateUserRequest.class), 1L)).willReturn(response);

        // when & then
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname")
                        .value(request.getNickname()))
                .andExpect(jsonPath("$.data.profile_img")
                        .value(request.getProfileImg()));

        verify(userService).updateUser(any(UpdateUserRequest.class), 1L);
    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 변경 사항 없음")
    @WithMockUser(username = "1", roles = "USER")
    void updateUserFail_ifNotExistsDiff() throws Exception {
        // given
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname(null)
                .profileImg(null)
                .build();

        // when & then
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).updateUser(any(UpdateUserRequest.class), any());
    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 닉네임 비어 있음")
    @WithMockUser(username = "1", roles = "USER")
    void updateUserFail_ifNicknameBlank() throws Exception {
        // given
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname("  ")
                .profileImg("profile.png")
                .build();

        // when & then
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).updateUser(any(UpdateUserRequest.class), any());
    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 닉네임 중복")
    @WithMockUser(username = "1", roles = "USER")
    void updateUserFail_ifExistsNickname() throws Exception {
        // given
        UpdateUserRequest request = UpdateUserRequest.builder()
                .nickname("중복닉네임")
                .profileImg("profile.png")
                .build();
        given(userService.updateUser(any(UpdateUserRequest.class), 1L)).willThrow(new RuntimeException("회원 정보 수정 실패 - 닉네임 중복. "));

        // when & then
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService).updateUser(any(UpdateUserRequest.class), 1L);
    }


    @Test
    @DisplayName("비밀번호 수정 성공 테스트")
    @WithMockUser(username = "1", roles = "USER")
    void updatePasswordSuccess() throws Exception {
        // given
        UpdatePasswordRequest request =
                UpdatePasswordRequest.builder()
                        .currentPassword("Password1!")
                        .newPassword("NewPassword1!")
                        .build();

        UpdatePasswordResponse response = mock(UpdatePasswordResponse.class);
        given(userService.updatePassword(any(UpdatePasswordRequest.class), 1L)).willReturn(response);

        // when & then
        mockMvc.perform(patch("/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        verify(userService).updatePassword(any(UpdatePasswordRequest.class), 1L);
    }

    @Test
    @DisplayName("비밀번호 수정 실패 테스트 - 현재 비밀번호 비어 있음")
    @WithMockUser(username = "1", roles = "USER")
    void updatePasswordFail_ifCurrentPasswordBlank()
            throws Exception {

        // given
        UpdatePasswordRequest request =
                UpdatePasswordRequest.builder()
                        .currentPassword("")
                        .newPassword("NewPassword1!")
                        .build();

        // when & then
        mockMvc.perform(patch("/users/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verify(userService, never()).updatePassword(any(UpdatePasswordRequest.class), any());
    }

    @Test
    @DisplayName("비밀번호 수정 실패 테스트 - 새 비밀번호 형식 오류")
    @WithMockUser(username = "1", roles = "USER")
    void updatePasswordFail_ifNewPasswordInvalid()
            throws Exception {

        // given
        UpdatePasswordRequest request =
                UpdatePasswordRequest.builder()
                        .currentPassword("Password1!")
                        .newPassword("1234")
                        .build();

        // when & then
        mockMvc.perform(patch("/users/me/password").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());

        verify(userService, never()).updatePassword(any(UpdatePasswordRequest.class), any());
    }

    @Test
    @DisplayName("비밀번호 수정 실패 테스트 - 회원 없음")
    @WithMockUser(username = "1", roles = "USER")
    void updatePasswordFail_ifUserNotFound()
            throws Exception {

        // given
        UpdatePasswordRequest request =
                UpdatePasswordRequest.builder()
                        .currentPassword("Password1!")
                        .newPassword("NewPassword1!")
                        .build();

        given(userService.updatePassword(any(UpdatePasswordRequest.class), 1L)).willThrow(new RuntimeException("비밀번호 수정 실패 - 회원 없음."));

        // when & then
        mockMvc.perform(patch("/users/me/password").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
        verify(userService).updatePassword(any(UpdatePasswordRequest.class), 1L);
    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    @WithMockUser(username = "1", roles = "USER")
    void deleteUserSuccess() throws Exception {
        // when & then
        mockMvc.perform(delete("/users/me")).andExpect(status().isOk());
        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("회원 탈퇴 실패 테스트 - 회원 없음")
    @WithMockUser(username = "1", roles = "USER")
    void deleteUserFail_ifUserNotFound() throws Exception {
        // given
        given(userService.deleteUser(1L)).willThrow(new RuntimeException("회원 탈퇴 실패 - 회원 없음."));

        // when & then
        mockMvc.perform(delete("/users/me")).andExpect(status().isBadRequest());
        verify(userService).deleteUser(1L);
    }
}