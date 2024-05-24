package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.BlogAPIException;
import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.CommentDTO;
import com.BlogifyHub.model.DTO.ProfileResponseDTO;
import com.BlogifyHub.model.entity.Comment;
import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.entity.enums.Role;
import com.BlogifyHub.model.mapper.CommentMapper;
import com.BlogifyHub.repository.CommentRepository;
import com.BlogifyHub.repository.PostRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private CommentMapper commentMapper;

    private UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              CommentMapper commentMapper,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }


    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));

        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL(),
                user.getFollowersNumber()
        );
        Comment comment = commentMapper.mapToEntity(commentDTO);
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        comment.setPost(post);
        comment.setUser(user);
        Comment newComment = commentRepository.save(comment);
        CommentDTO savedCommentDTO = commentMapper.mapToDTO(newComment);
        savedCommentDTO.setProfileResponseDTO(profileResponseDTO);
        return savedCommentDTO;
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::mapToCommentDTOWithProfile).collect(Collectors.toList());
    }

    private CommentDTO mapToCommentDTOWithProfile(Comment comment) {
        CommentDTO commentDTO = commentMapper.mapToDTO(comment);

        User user = comment.getUser();
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL(),
                user.getFollowersNumber()
        );
        commentDTO.setProfileResponseDTO(profileResponseDTO);

        return commentDTO;
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
        CommentDTO commentDTO = commentMapper.mapToDTO(comment);

        User user = comment.getUser();
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL(),
                user.getFollowersNumber()
        );
        commentDTO.setProfileResponseDTO(profileResponseDTO);

        return commentDTO;
    }

    @Override
    public CommentDTO updateComment(long postId, long commentId, CommentDTO commentDTO, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));

        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfilePictureURL(),
                user.getFollowersNumber()
        );
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        comment.setBody(commentDTO.getBody());
        Comment updatedComment = commentRepository.save(comment);
        CommentDTO updatedCommentDTO = commentMapper.mapToDTO(updatedComment);
        updatedCommentDTO.setProfileResponseDTO(profileResponseDTO);
        return updatedCommentDTO;
    }

    /**
     * in this context, Authentication refers to the currently logged-in user.
     * The Authentication object provides details about the authenticated user making the request.
     * When you call authentication.getName(), it returns the email (or username) of the logged-in user.
     * This user might be the owner of the comment, the owner of the post, or an admin.
     * */
    @Override
    public void deleteComment(Long postId, Long commentId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isCommentOwner = user.equals(comment.getUser());
        boolean isPostOwner = user.equals(post.getUser());

        if (!isAdmin && !isCommentOwner && !isPostOwner) {
            throw new BlogAPIException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this comment");
        }
        commentRepository.deleteById(commentId);
    }
}
