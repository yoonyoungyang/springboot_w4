package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostByPostIdAndDeletedAtIsNull(Long PostId);

    @Query("select p from Post p join fetch p.user where p.deletedAt is null order by p.createdAt desc")
    List<Post> findAllWithUser();
}