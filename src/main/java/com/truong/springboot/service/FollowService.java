package com.truong.springboot.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truong.springboot.entity.Follow;
import com.truong.springboot.entity.User;
import com.truong.springboot.repository.FollowRepository;
import com.truong.springboot.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class FollowService {
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void followUser(User followingUser, User followedUser) {
        if (followingUser.getId().equals(followedUser.getId())) {
            throw new RuntimeException("You cannot follow yourself");
        }
        if (followRepository.findByFollowingUserAndFollowedUser(followingUser, followedUser).isPresent()) {
            throw new RuntimeException("You are already following this user");
        }

        Follow follow = new Follow();
        follow.setFollowingUser(followingUser);
        follow.setFollowedUser(followedUser);
        follow.setCreatedAt(LocalDateTime.now());
        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(User followingUser, User followedUser) {
        Follow follow = followRepository.findByFollowingUserAndFollowedUser(followingUser, followedUser)
            .orElseThrow(() -> new RuntimeException("You are not following this user"));
        followRepository.delete(follow);
    }

    public List<User> getFollowers(User user) {
        return followRepository.findByFollowedUser(user)
            .stream()
            .map(Follow::getFollowingUser)
            .collect(Collectors.toList());
    }

    public List<User> getFollowing(User user) {
        return followRepository.findByFollowingUser(user)
            .stream()
            .map(Follow::getFollowedUser)
            .collect(Collectors.toList());
    }

    public boolean isFollowing(User followingUser, User followedUser) {
        return followRepository.findByFollowingUserAndFollowedUser(followingUser, followedUser).isPresent();
    }
}
