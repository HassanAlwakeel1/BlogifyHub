package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.entity.Follow;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.repository.FollowRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {
    private UserRepository userRepository;
    private FollowRepository followRepository;

    public FollowServiceImpl(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @Override
    public void follow(Long userId, Authentication authentication) {
        String email = authentication.getName();
        User follower = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Invalid token"));

        User followedUser = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));

        if (follower.equals(followedUser)){
            throw new RuntimeException("You can't follow yourself");
        }

        if (isFollowed(follower,followedUser)){
            throw new RuntimeException("You can't follow the same person twice, you already following him");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowedUser(followedUser);
        followRepository.save(follow);
    }

    @Override
    public void unFollow(Long userId, Authentication authentication) {
        String email = authentication.getName();
        User follower = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Invalid token"));
        User followedUser = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("UserId","id",userId));

        if (isFollowed(follower,followedUser)){
            Follow follow = followRepository.findByFollowerAndFollowedUser(follower,followedUser);
            followRepository.delete(follow);
        }else throw new RuntimeException("Your are not following this person to unfollow him");
    }

    private boolean isFollowed(User follower, User followedUser){
        Follow follow = followRepository.findByFollowerAndFollowedUser(follower,followedUser);

        if (follow == null){
            return false;
        }
        return true;
    }
}
