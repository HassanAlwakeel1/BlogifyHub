package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.PostList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PostListRepository extends JpaRepository<PostList,Long> {
    Set<PostList> findByUserId(Long userId);
}
