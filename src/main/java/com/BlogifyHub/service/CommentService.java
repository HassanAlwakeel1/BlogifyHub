package com.BlogifyHub.service;

import com.BlogifyHub.model.DTO.CommentDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(long postId, CommentDTO commentDTO, Authentication authentication);

    List<CommentDTO> getCommentsByPostId(long postId);

    CommentDTO getCommentById(long postId, long commentId);

    CommentDTO updateComment(long postId, long commentId, CommentDTO commentDTO);

    void deleteComment(Long postId, Long commentId);
}
