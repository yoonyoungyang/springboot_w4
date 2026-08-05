package kr.adapterz.springboot;

import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.entity.Comment;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.CommentRepository;
import kr.adapterz.springboot.repository.PostRepository;
import kr.adapterz.springboot.repository.UserRepository;
import kr.adapterz.springboot.security.TokenProvider;
import kr.adapterz.springboot.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpSuccess() {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        User savedUser = User.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        //when
        SignUpResponse response = userService.signup(request);
        //then
        verify(userRepository).save(any(User.class));
        assertThat(response.getNickname()).isEqualTo(request.getNickname());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getProfileImg()).isEqualTo(request.getProfileImg());

    }

    @Test
    @DisplayName("회원가입 이메일 중복 테스트")
    void signUp_ifExistsEmail() {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

        assertThatThrownBy(() -> userService.signup(request)).isInstanceOf(RuntimeException.class).hasMessage("회원가입 실패 - 이미 사용 중인 이메일");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 닉네임 중복 테스트")
    void signUp_ifExistsNickname() {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();
        given(userRepository.existsByNicknameAndDeletedAtIsNull(request.getNickname())).willReturn(true);

        //when&then
        assertThatThrownBy(() -> userService.signup(request)).isInstanceOf(RuntimeException.class).hasMessage("회원가입 실패 - 이미 사용 중인 닉네임");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 비밀번호 암호화 테스트")
    void signUp_isPasswordEncoded() {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();

        User signupUser = User.builder()
                .email("test@email.com")
                .password("Password1!")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();


        given(userRepository.save(any(User.class))).willReturn(signupUser);
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(userRepository.existsByNicknameAndDeletedAtIsNull(request.getNickname())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword");

        //when
        userService.signup(request);

        //then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .build();
        ReflectionTestUtils.setField(user, "userId", 1L);

        given(userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(true);
        given(tokenProvider.createToken(String.format("%s:%s", user.getUserId(), user.getRole()))).willReturn("token");
        //when
        LoginResponse response = userService.login(request);
        //then
        verify(userRepository).findByEmailAndDeletedAtIsNull(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getToken()).isEqualTo("token");
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 이메일 검사")
    void loginFail_ifNotExistsEmail() {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .build();
        given(userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())).willReturn(Optional.empty());
        //when&then
        assertThatThrownBy(()-> userService.login(request)).isInstanceOf(RuntimeException.class).hasMessage("로그인 실패 - 사용자 정보 찾을 수 없음.");
        verify(userRepository).findByEmailAndDeletedAtIsNull(request.getEmail());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(tokenProvider, never()).createToken(any());
    }
    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 검사")
    void loginFail_ifNotMatchesPassword() {
        //given
        LoginRequest request = LoginRequest.builder()
                .email("test@email.com")
                .password("Password1!")
                .build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .build();
        given(userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(false);

        //when&then
        assertThatThrownBy(()-> userService.login(request)).isInstanceOf(RuntimeException.class).hasMessage("로그인 실패 - 사용자 정보 찾을 수 없음.");
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder).matches(any(), any());
        verify(tokenProvider, never()).createToken(any());

    }

    @Test
    @DisplayName("회원 정보 조회 성공 테스트")
    void getUserSuccess() {
        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .profileImg("url")
                .build();


        //given
        ReflectionTestUtils.setField(user, "userId", 1L);
        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.of(user));
        //when
        UserInfoResponse response = userService.getUser(user.getUserId());
        //then
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getProfileImg()).isEqualTo(user.getProfileImg());
    }

    @Test
    @DisplayName("회원 정보 조회 실패 테스트 - 이메일 검사")
    void getUserFail_ifNotExistsEMail() {
        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .build();
        //given
        ReflectionTestUtils.setField(user, "userId", 1L);
        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.empty());
        //when&then
        assertThatThrownBy(() -> userService.getUser(user.getUserId())).isInstanceOf(RuntimeException.class).hasMessage("존재하지 않는 사용자입니다.");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("회원 정보 수정 성공 테스트")
    void updateUserSuccess() {
        UpdateUserRequest request = UpdateUserRequest.builder().nickname("테스트뉴닉네임").profileImg("urlurl").build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .build();
        //given
        ReflectionTestUtils.setField(user, "userId", 1L);
        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.of(user));
        given(userRepository.existsByNicknameAndDeletedAtIsNull(request.getNickname())).willReturn(false);
        //when
        UpdateUserResponse response = userService.updateUser(request, user.getUserId());
        //then
        verify(userRepository).findUserByUserIdAndDeletedAtIsNull(user.getUserId());
        verify(userRepository).existsByNicknameAndDeletedAtIsNull(request.getNickname());
        assertThat(response.getNickname()).isEqualTo(request.getNickname());
        assertThat(response.getProfileImg()).isEqualTo(request.getProfileImg());
        assertThat(user.getNickname()).isEqualTo(response.getNickname());
        assertThat(user.getProfileImg()).isEqualTo(response.getProfileImg());
    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 회원 없음")
    void updateUserFail_ifUserNotFound() {
        UpdateUserRequest request = UpdateUserRequest.builder().nickname("테스트닉네임").profileImg("url").build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .build();
        //given
        ReflectionTestUtils.setField(user, "userId", 1L);
        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.empty());
        //when&then
        assertThatThrownBy(() -> userService.updateUser(request, user.getUserId())).isInstanceOf(RuntimeException.class).hasMessage("회원 정보 수정 실패 - 회원 없음.");
        verify(userRepository).findUserByUserIdAndDeletedAtIsNull(user.getUserId());
        verify(userRepository, never()).existsByNicknameAndDeletedAtIsNull(request.getNickname());
    }
    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 닉네임과 프로필url null로 들어옴")
    void updateUserFail_ifNotExistsDiff() {
        UpdateUserRequest request = UpdateUserRequest.builder().nickname(null).profileImg(null).build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .profileImg("url")
                .build();
        //given
        ReflectionTestUtils.setField(user, "userId", 1L);
        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.of(user));
        //when&then
        assertThatThrownBy(() -> userService.updateUser(request, user.getUserId())).isInstanceOf(RuntimeException.class).hasMessage("회원 정보 수정 실패 - 변경 사항 없음");
        verify(userRepository).findUserByUserIdAndDeletedAtIsNull(user.getUserId());
        assertThat(user.getNickname()).isEqualTo("테스트닉네임");
        assertThat(user.getProfileImg()).isEqualTo("url");

    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 닉네임 비어 있음")
    void updateUserFail_ifNicknameBlank() {
        UpdateUserRequest request = UpdateUserRequest.builder().nickname("  ").profileImg(null).build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .build();
        //given
        ReflectionTestUtils.setField(user, "userId", 1L);
        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.of(user));

        //when&then
        assertThatThrownBy(() -> userService.updateUser(request, user.getUserId())).isInstanceOf(RuntimeException.class).hasMessage("회원 정보 수정 실패 - 닉네임이 비어 있음.");
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository).findUserByUserIdAndDeletedAtIsNull(user.getUserId());
        verify(userRepository, never()).existsByNicknameAndDeletedAtIsNull(any());
        assertThat(user.getNickname()).isEqualTo("테스트닉네임");
    }

    @Test
    @DisplayName("회원 정보 수정 실패 테스트 - 닉네임 중복")
    void updateUserFail_ifExistsNickname() {
        UpdateUserRequest request = UpdateUserRequest.builder().nickname("중복닉네임").profileImg("url").build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .profileImg("url")
                .build();
        //given
        ReflectionTestUtils.setField(user, "userId", 1L);
        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.of(user));
        given(userRepository.existsByNicknameAndDeletedAtIsNull(request.getNickname())).willReturn(true);
        //when&then
        assertThatThrownBy(() -> userService.updateUser(request, user.getUserId())).isInstanceOf(RuntimeException.class).hasMessage("회원 정보 수정 실패 - 닉네임 중복.");
        verify(userRepository, never()).save(any(User.class));
        verify(userRepository).existsByNicknameAndDeletedAtIsNull(request.getNickname());
        assertThat(user.getNickname()).isEqualTo("테스트닉네임");
        assertThat(user.getProfileImg()).isEqualTo("url");
    }

    @Test
    @DisplayName("비밀번호 변경 성공 테스트")
    void updatePasswordSuccess() {
        // given
        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword("Password1!")
                .newPassword("NewPassword1!")
                .build();

        User user = User.builder()
                .email("test@email.com")
                .password("encodedOldPassword")
                .nickname("테스트닉네임")
                .build();

        ReflectionTestUtils.setField(user, "userId", 1L);

        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())).willReturn(true);
        given(passwordEncoder.encode(request.getNewPassword())).willReturn("encodedNewPassword");

        // when
        UpdatePasswordResponse response = userService.updatePassword(request, user.getUserId());

        // then
        verify(userRepository).findUserByUserIdAndDeletedAtIsNull(user.getUserId());
        verify(passwordEncoder).matches(request.getCurrentPassword(), "encodedOldPassword");
        verify(passwordEncoder).encode(request.getNewPassword());
        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
    }

    @Test
    @DisplayName("비밀번호 변경 실패 테스트 - 회원 없음")
    void updatePasswordFail_ifUserNotFound() {
        // given

        UpdatePasswordRequest request = UpdatePasswordRequest.builder()
                .currentPassword("Password1!")
                .newPassword("NewPassword1!")
                .build();
        User user = User.builder()
                .email("test@email.com")
                .password("encodedOldPassword")
                .nickname("테스트닉네임")
                .build();

        ReflectionTestUtils.setField(user, "userId", 1L);

        given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.updatePassword(request, user.getUserId())).isInstanceOf(RuntimeException.class).hasMessage("비밀번호 변경 실패 - 회원 없음.");
        verify(userRepository).findUserByUserIdAndDeletedAtIsNull(user.getUserId());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    void deleteUserSuccess() {
        // given

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();


        ReflectionTestUtils.setField(user, "userId", 1L);

        Post post = Post.builder()
                .user(user)
                .title("테스트 게시글")
                .content("테스트 게시글 내용")
                .contentImg(null)
                .build();

        ReflectionTestUtils.setField(post, "postId", 1L);

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content("테스트 댓글")
                .build();

        ReflectionTestUtils.setField(comment, "commentId", 1L);

                given(userRepository.findUserByUserIdAndDeletedAtIsNull(user.getUserId())).willReturn(Optional.of(user));
        given(postRepository.findAllByUser_UserIdAndDeletedAtIsNull(user.getUserId()))
                .willReturn(List.of(post));

        given(commentRepository.findAllByUser_UserIdAndDeletedAtIsNull(user.getUserId()))
                .willReturn(List.of(comment));

        // when
        userService.deleteUser(user.getUserId());

        // then
        verify(userRepository).findUserByUserIdAndDeletedAtIsNull(user.getUserId());
        assertThat(user.getDeletedAt()).isNotNull();
        assertThat(post.getDeletedAt()).isNotNull();
        assertThat(comment.getDeletedAt()).isNotNull();


    }
    @Test
    @DisplayName("회원 탈퇴 실패 테스트 - 회원 없음")
    void deleteUserFail_ifUserNotFound() {
        // given
        Long loginUserId = 1L;

        User user = User.builder()
                .email("test@email.com")
                .password("encodedPassword")
                .nickname("테스트닉네임")
                .profileImg("profile.png")
                .build();


        given(userRepository.findUserByUserIdAndDeletedAtIsNull(loginUserId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.deleteUser(loginUserId)).isInstanceOf(RuntimeException.class).hasMessage("회원 탈퇴 실패 - 회원 없음.");
        verify(userRepository).findUserByUserIdAndDeletedAtIsNull(loginUserId);
        verify(postRepository, never())
                .findAllByUser_UserIdAndDeletedAtIsNull(any());

        verify(commentRepository, never())
                .findAllByUser_UserIdAndDeletedAtIsNull(any());

    }

}
