package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.entity.Comment;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.repository.CommentRepository;
import kr.adapterz.springboot.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public CreateCommentResponse createComment(int postId,
                                               CreateCommentRequest request) {

        Post post = postRepository.findPostById(postId);

        if (post == null) {
            throw new RuntimeException("게시글이 존재하지 않습니다.");
        }

        int commentId = commentRepository.nextCommentId();

        Comment comment = new Comment(
                commentId,
                postId,
                request.getUser_id(),
                request.getContent()
        );

        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse(savedComment);
    }

    public UpdateCommentResponse updateComment(int commentId,
                                               UpdateCommentRequest request) {

        Comment comment = commentRepository.findCommentById(commentId);

        if (comment == null) {
            throw new RuntimeException("댓글이 존재하지 않습니다.");
        }

        if (comment.getUserId() != request.getUser_id()) {
            throw new RuntimeException("댓글 수정 권한이 없습니다.");
        }

        comment.updateComment(request.getContent());

        return new UpdateCommentResponse(comment);
    }

    public DeleteCommentResponse deleteComment(int commentId,
                                               int userId) {

        Comment comment = commentRepository.findCommentById(commentId);

        if (comment == null) {
            throw new RuntimeException("댓글이 존재하지 않습니다.");
        }

        if (comment.getUserId() != userId) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.deleteComment(commentId);

        return new DeleteCommentResponse(commentId);
    }
}