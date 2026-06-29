package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.Like;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class LikeRepository {

    private final Map<Integer, Like> likes = new HashMap<>();
    private int sequence = 1;

    public int nextLikeId() {
        return sequence++;
    }

    public Like save(Like like) {
        likes.put(like.getLikeId(), like);
        return like;
    }

    public Like findLike(int postId, int userId) {

        for (Like like : likes.values()) {
            if (like.getPostId() == postId &&
                    like.getUserId() == userId) {

                return like;
            }
        }

        return null;
    }

    public void deleteLike(int likeId) {
        likes.remove(likeId);
    }
}