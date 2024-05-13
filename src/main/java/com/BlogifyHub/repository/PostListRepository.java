package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.PostList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostListRepository extends JpaRepository<PostList,Long> {
}
