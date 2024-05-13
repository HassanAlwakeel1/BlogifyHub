package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.Post;
import com.BlogifyHub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByCategoryId(Long categoryId);
}
