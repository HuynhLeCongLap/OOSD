package com.truong.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.truong.springboot.entity.Post;
import com.truong.springboot.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findAllByOrderByCreatedAtDesc();
}
