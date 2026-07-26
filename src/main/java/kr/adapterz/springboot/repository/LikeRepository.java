package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.entity.PostLike;
import kr.adapterz.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<PostLike> findByUserAndPost(User user, Post post);

    boolean  existsPostLikeByUserIdAndPostId(Long userId, Long postId);
}