package com.truong.springboot.service;

import java.util.List;

import com.truong.springboot.entity.User;

public interface UserService {
    User findByUsername(String username);
    User saveUser(User user);
    User findById(Long id);
    List<User> findAll();
}
