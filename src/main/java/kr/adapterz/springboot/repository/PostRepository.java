package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostByPostIdAndDeletedAtIsNull(Long PostId);
}