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

import java.util.List;

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

    public CreateCommentResponse createComment(Long postId, CreateCommentRequest request, Long loginUserId) {
        Post post = postRepository.findPostByPostIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("댓글 달기 실패  - 게시글 없음."));
        User user = userRepository.findUserByUserIdAndDeletedAtIsNull(loginUserId)
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

    public UpdateCommentResponse updateComment(Long commentId, UpdateCommentRequest request, Long loginUserId) {
        Comment comment = commentRepository.findCommentByCommentIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 수정 실패 - 댓글 없음."));

        if (!comment.getUser().getUserId().equals(loginUserId)) {
            throw new RuntimeException("댓글 수정 실패 - 댓글 수정 권한 없음.");
        }

        comment.updateComment(request.getContent());

        return new UpdateCommentResponse(comment);
    }

    public DeleteCommentResponse deleteComment(Long commentId, Long loginUserId) {
        Comment comment = commentRepository.findCommentByCommentIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 삭제 실패 - 댓글 없음."));

        if (!comment.getUser().getUserId().equals(loginUserId)) {
            throw new RuntimeException("댓글 삭제 실패 - 댓글 삭제 권한 없음.");
        }

        comment.softDelete();
        comment.getPost().decreaseCommentCount();

        return new DeleteCommentResponse(comment);
    }

    @Transactional(readOnly = true)
    public CommentListResponse commentList(Long postId, Long loginUserId) {
        Post post = postRepository.findPostByPostIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new RuntimeException("댓글 목록 조회 실패 - 게시글 없음."));
        List<Comment> comments =
                commentRepository.findAllWithUserByPost(post);
        Long totalCount = commentRepository.countByPostAndDeletedAtIsNull(post);

        List<CommentResponse> responses = comments.stream()
                .map(comment -> new CommentResponse(comment, loginUserId))
                .toList();

        return new CommentListResponse(responses, totalCount);
    }
}