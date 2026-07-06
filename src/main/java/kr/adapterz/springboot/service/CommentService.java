package kr.adapterz.springboot.service;

import kr.adapterz.springboot.dto.*;
import kr.adapterz.springboot.entity.Comment;
import kr.adapterz.springboot.entity.Post;
import kr.adapterz.springboot.entity.User;
import kr.adapterz.springboot.repository.CommentRepository;
import kr.adapterz.springboot.repository.PostRepository;
import kr.adapterz.springboot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CreateCommentResponse createComment(Long postId, CreateCommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("댓글 달기 실패  - 게시글 없음."));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("댓글 달기 실패 - 회원 없음."));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        post.increaseCommentCount();

        return new CreateCommentResponse(savedComment);
    }

    public UpdateCommentResponse updateComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 수정 실패 - 댓글 없음."));

        if (!comment.getUser().getUserId().equals(request.getUserId())) {
            throw new RuntimeException("댓글 수정 실패 - 댓글 수정 권한 없음.");
        }

        comment.updateComment(request.getContent());

        return new UpdateCommentResponse(comment);
    }

    public DeleteCommentResponse deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 삭제 실패 - 댓글 없음."));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("댓글 삭제 실패 - 댓글 삭제 권한 없음.");
        }

        comment.softDelete();
        comment.getPost().decreaseCommentCount();

        return new DeleteCommentResponse(comment);
    }
}