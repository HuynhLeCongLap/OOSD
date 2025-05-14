package com.truong.springboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truong.springboot.entity.Follow;
import com.truong.springboot.entity.User;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowingUserAndFollowedUser(User followingUser, User followedUser);
    List<Follow> findByFollowedUser(User followedUser);
    List<Follow> findByFollowingUser(User followingUser);
}
