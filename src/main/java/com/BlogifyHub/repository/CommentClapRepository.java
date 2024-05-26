package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.Comment;
import com.BlogifyHub.model.entity.CommentClap;
import com.BlogifyHub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentClapRepository extends JpaRepository<CommentClap, Long> {

    Optional<CommentClap> findByUserAndComment(User user, Comment comment);
}
