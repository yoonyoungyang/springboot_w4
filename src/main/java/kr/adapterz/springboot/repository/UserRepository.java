package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    private final Map<Integer, User> users = new HashMap<>();
    private int sequence = 1;

    public int nextUserId() {
        return sequence++;
    }

    public boolean existsByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

    public boolean existsByNickname(String nickname) {
        for (User user : users.values()) {
            if (user.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;

    }

    public User findUserByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public User save(User user) {
        users.put(user.getUserId(), user);
        return user;
    }

    public User findUserById(int userId) {

        return users.get(userId);

    }

    public void deleteUser(int userId) {
        users.remove(userId);
    }
}