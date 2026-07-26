package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.Comment;
import kr.adapterz.springboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostAndDeletedAtIsNullOrderByCreatedAtAsc(Post post);
    Optional<Comment> findCommentByCommentIdAndDeletedAtIsNull(Long commentId);
}