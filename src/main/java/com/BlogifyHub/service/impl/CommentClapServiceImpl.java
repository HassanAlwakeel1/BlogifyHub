package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.entity.Comment;
import com.BlogifyHub.model.entity.CommentClap;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.repository.CommentClapRepository;
import com.BlogifyHub.repository.CommentRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.ClapService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Qualifier("commentClapService")
public class CommentClapServiceImpl implements ClapService {
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private CommentClapRepository commentClapRepository;

    public CommentClapServiceImpl(UserRepository userRepository,
                                  CommentRepository commentRepository,
                                  CommentClapRepository commentClapRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentClapRepository = commentClapRepository;
    }

    @Override
    public void clap(Long commentId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","commentId",commentId));
        if (user.equals(comment.getUser())){
            throw new RuntimeException("You can't clap to your own comment.");
        }

        Optional<CommentClap> optionalClap = commentClapRepository.findByUserAndComment(user, comment);
        CommentClap clap;
        if (optionalClap.isPresent()){
            clap = optionalClap.get();
            if (clap.getNumberOfClaps() >= 50){
                throw new RuntimeException("You can't clap more than 50 times on the same comment.");
            }
            clap.setNumberOfClaps(clap.getNumberOfClaps() + 1);
        }else {
            clap = new CommentClap();
            clap.setUser(user);
            clap.setComment(comment);
            clap.setNumberOfClaps(1);
            comment.getCommentClaps().add(clap);
            comment.setNumberOfClappers(comment.getNumberOfClappers() + 1);
        }
        comment.setNumberOfClaps(comment.getNumberOfClaps() + 1);
        commentRepository.save(comment);
        commentClapRepository.save(clap);
    }

    @Override
    public void removeUserClaps(Long commentId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","commentId",commentId));

        Optional<CommentClap> optionalClap = commentClapRepository.findByUserAndComment(user, comment);
        CommentClap clap;
        if (optionalClap.isPresent()){
            clap = optionalClap.get();
            comment.setNumberOfClaps(comment.getNumberOfClaps() - clap.getNumberOfClaps());
            comment.setNumberOfClappers(comment.getNumberOfClappers() - 1);
            commentClapRepository.delete(clap);
            commentRepository.save(comment);
        }else throw new RuntimeException("Clap not found");
    }
}
