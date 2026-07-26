package kr.adapterz.springboot.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;


    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", unique = true, nullable = false)
    @Size(max = 10, message = "닉네임은 최대 10자까지 가능합니다.")
    private String nickname;

    @Column(name = "profile_img")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private
    UserRole role;

    @Builder
    private User(String email, String password, String nickname, String profileImg) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.role = UserRole.USER;
    }


    public void updateUser(String nickname, String profileImg) {
        if (nickname != null) {
            this.nickname = nickname;
        }

        if (profileImg != null) {
            this.profileImg = profileImg;
        }
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}