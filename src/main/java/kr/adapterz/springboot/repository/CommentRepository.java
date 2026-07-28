package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.Comment;
import kr.adapterz.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.user where c.post = :post and c.deletedAt is null order by c.createdAt asc")
    List<Comment> findAllWithUserByPost(@Param("post") Post post);

    Optional<Comment> findCommentByCommentIdAndDeletedAtIsNull(Long commentId);

    Long countByPostAndDeletedAtIsNull(Post post);
}