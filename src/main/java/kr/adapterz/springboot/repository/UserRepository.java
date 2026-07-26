package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNicknameAndDeletedAtIsNull(String nickname);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    Optional<User> findUserByUserIdAndDeletedAtIsNull(Long UserId);
}