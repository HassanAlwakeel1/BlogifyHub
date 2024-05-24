package com.BlogifyHub.service.impl;

import com.BlogifyHub.exception.ResourceNotFoundException;
import com.BlogifyHub.model.DTO.ProfileDTO;
import com.BlogifyHub.model.entity.Follow;
import com.BlogifyHub.model.entity.User;
import com.BlogifyHub.model.mapper.UserMapper;
import com.BlogifyHub.repository.FollowRepository;
import com.BlogifyHub.repository.UserRepository;
import com.BlogifyHub.service.FollowService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {
    private UserRepository userRepository;
    private FollowRepository followRepository;

    private UserMapper userMapper;

    public FollowServiceImpl(UserRepository userRepository, FollowRepository followRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.userMapper = userMapper;
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

        // Increment followersNumber of the followed user
        if (followedUser.getFollowersNumber()==null){
            followedUser.setFollowersNumber(0);
        }
        followedUser.setFollowersNumber(followedUser.getFollowersNumber() + 1);
        userRepository.save(followedUser);

        // Increment followingNumber of the follower
        if (follower.getFollowingNumber()==null){
            follower.setFollowingNumber(0);
        }
        follower.setFollowingNumber(follower.getFollowingNumber() + 1);
        userRepository.save(follower);
    }

    @Override
    public void unFollow(Long userId, Authentication authentication) {
        String email = authentication.getName();
        User follower = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("Invalid token"));
        User followedUser = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));

        if (isFollowed(follower,followedUser)){
            Follow follow = followRepository.findByFollowerAndFollowedUser(follower,followedUser);
            followRepository.delete(follow);

            // Decrement followersNumber of the followed user
            followedUser.setFollowersNumber(followedUser.getFollowersNumber() - 1);
            userRepository.save(followedUser);

            // Decrement followingNumber of the follower
            follower.setFollowingNumber(follower.getFollowingNumber() - 1);
            userRepository.save(follower);
        }else throw new RuntimeException("Your are not following this person to unfollow him");
    }

    @Override
    public List<ProfileDTO> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        List<Follow> followList = followRepository.findByFollowedUser(user);
        List<User> followers = followList.stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());

        List<ProfileDTO> followersProfileDTOs = followers.stream()
                .map(userMapper::userToProfileDTO)
                .collect(Collectors.toList());
        return followersProfileDTOs;
    }

    private boolean isFollowed(User follower, User followedUser){
        Follow follow = followRepository.findByFollowerAndFollowedUser(follower,followedUser);

        if (follow == null){
            return false;
        }
        return true;
    }
}
