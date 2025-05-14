package com.truong.springboot.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.truong.springboot.entity.User;
import com.truong.springboot.service.FollowService;
import com.truong.springboot.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @GetMapping
    public String listUsers(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        List<User> users = userService.findAll();
        Map<Long, Boolean> isFollowing = new HashMap<>();
        for (User user : users) {
            isFollowing.put(user.getId(), followService.isFollowing(currentUser, user));
        }
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isFollowing", isFollowing);
        return "user_list";
    }
}
