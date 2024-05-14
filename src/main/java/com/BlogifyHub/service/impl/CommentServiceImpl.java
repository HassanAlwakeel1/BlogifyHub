package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.BlogAPIException;
import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.CommentDTO;
import com.BlogifyHub.model.entity.Comment;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.mapper.CommentMapper;
import com.BlogifyHub.repository.CommentRepository;
import com.BlogifyHub.repository.PostRepository;
import com.BlogifyHub.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }


    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO) {
        Comment comment = commentMapper.mapToEntity(commentDTO);
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return commentMapper.mapToDTO(newComment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> commentMapper.mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return commentMapper.mapToDTO(comment);
    }

    @Override
    public CommentDTO updateComment(long postId, long commentId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        comment.setName(commentDTO.getName());
        comment.setBody(commentDTO.getBody());
        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        commentRepository.deleteById(commentId);
    }
}
