package kr.adapterz.springboot;

import jakarta.persistence.EntityManager;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("save, findByID 성공 테스트")
    void saveAndFindByIdSuccess() {
        // given
        User user = createUser(
                "test@email.com",
                "encodedPassword",
                "테스트닉네임"
        );

        // when
        User savedUser = userRepository.saveAndFlush(user);

        entityManager.clear();

        User foundUser = userRepository.findById(savedUser.getUserId())
                .orElseThrow();

        // then
        assertThat(foundUser.getUserId()).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("test@email.com");
        assertThat(foundUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(foundUser.getNickname()).isEqualTo("테스트닉네임");
        assertThat(foundUser.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("existsByEmail 성공 테스트")
    void existsByEmailSuccess() {
        // given
        User user = createUser(
                "test@email.com",
                "encodedPassword",
                "테스트닉네임"
        );

        userRepository.saveAndFlush(user);
        entityManager.clear();

        // when
        boolean result = userRepository.existsByEmail("test@email.com");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsByEmail 실패 테스트")
    void existsByEmailFail_ifUserNotFound() {
        // when
        boolean result = userRepository.existsByEmail("none@email.com");

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsByNicknameAndDeletedAtIsNull 성공 테스트")
    void existsByNicknameSuccess() {
        // given
        User user = createUser(
                "test@email.com",
                "encodedPassword",
                "테스트닉네임"
        );

        userRepository.saveAndFlush(user);
        entityManager.clear();

        // when
        boolean result =
                userRepository.existsByNicknameAndDeletedAtIsNull(
                        "테스트닉네임"
                );

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsByNicknameAndDeletedAtIsNull 실패 테스트")
    void existsByNicknameFail_ifUserNotFound() {
        // when
        boolean result =
                userRepository.existsByNicknameAndDeletedAtIsNull(
                        "없는닉네임"
                );

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("findByEmailAndDeletedAtIsNull 성공 테스트")
    void findByEmailSuccess() {
        // given
        User user = createUser(
                "test@email.com",
                "encodedPassword",
                "테스트닉네임"
        );

        userRepository.saveAndFlush(user);
        entityManager.clear();

        // when
        Optional<User> result =
                userRepository.findByEmailAndDeletedAtIsNull(
                        "test@email.com"
                );

        // then
        assertThat(result).isPresent();

        User foundUser = result.get();

        assertThat(foundUser.getEmail()).isEqualTo("test@email.com");
        assertThat(foundUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(foundUser.getNickname()).isEqualTo("테스트닉네임");
        assertThat(foundUser.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("findByEmailAndDeletedAtIsNull 실패 테스트")
    void findByEmailFail_ifUserNotFound() {
        // when
        Optional<User> result =
                userRepository.findByEmailAndDeletedAtIsNull(
                        "none@email.com"
                );

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("탈퇴 회원 이메일로 조회 불가 테스트")
    void findByEmailFail_ifDeletedUser() {
        // given
        User user = createUser(
                "test@email.com",
                "encodedPassword",
                "테스트닉네임"
        );

        User savedUser = userRepository.saveAndFlush(user);

        savedUser.softDelete();

        userRepository.flush();
        entityManager.clear();

        // when
        Optional<User> result =
                userRepository.findByEmailAndDeletedAtIsNull(
                        "test@email.com"
                );

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("회원 ID로 회원 조회 성공 테스트")
    void findUserByUserIdSuccess() {
        // given
        User user = createUser(
                "test@email.com",
                "encodedPassword",
                "테스트닉네임"
        );

        User savedUser = userRepository.saveAndFlush(user);
        Long userId = savedUser.getUserId();

        entityManager.clear();

        // when
        Optional<User> result =
                userRepository.findUserByUserIdAndDeletedAtIsNull(userId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(userId);
        assertThat(result.get().getDeletedAt()).isNull();
    }

    private User createUser(
            String email,
            String password,
            String nickname
    ) {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImg("profile.png")
                .build();
    }
}