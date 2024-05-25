package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.PostClap;
import com.BlogifyHub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostClapRepository extends JpaRepository<PostClap,Long> {

    Optional<PostClap> findByUserAndPost(User user, Post post);
}
