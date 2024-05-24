package com.BlogifyHub.repository;

import com.BlogifyHub.model.entity.Follow;
import com.BlogifyHub.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FollowRepository extends JpaRepository<Follow,Long> {
    @Query("SELECT f FROM Follow f WHERE (f.follower = :follower AND f.followedUser = :followedUser)")
    Follow findByFollowerAndFollowedUser(@Param("follower") User follower, @Param("followedUser") User followedUser);
}
