package kr.adapterz.springboot.repository;

import kr.adapterz.springboot.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentRepository {

    private final Map<Integer, Comment> comments = new HashMap<>();
    private int sequence = 1;

    public int nextCommentId() {
        return sequence++;
    }

    public Comment save(Comment comment) {
        comments.put(comment.getCommentId(), comment);
        return comment;
    }

    public Comment findCommentById(int commentId) {
        return comments.get(commentId);
    }

    public List<Comment> findCommentsByPostId(int postId) {
        List<Comment> commentList = new ArrayList<>();

        for (Comment comment : comments.values()) {
            if (comment.getPostId() == postId) {
                commentList.add(comment);
            }
        }

        return commentList;
    }

    public void deleteComment(int commentId) {
        comments.remove(commentId);
    }
}