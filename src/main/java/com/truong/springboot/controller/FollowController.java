package com.truong.springboot.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.truong.springboot.entity.User;
import com.truong.springboot.service.FollowService;
import com.truong.springboot.service.UserService;

@Controller
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showFollowList(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        List<User> followers = followService.getFollowers(currentUser);
        List<User> following = followService.getFollowing(currentUser);
        model.addAttribute("followers", followers);
        model.addAttribute("following", following);
        return "follow_list";
    }

    @PostMapping("/follow/{userId}")
    public String followUser(@PathVariable Long userId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userService.findByUsername(principal.getName());
            User userToFollow = userService.findById(userId);
            followService.followUser(currentUser, userToFollow);
            redirectAttributes.addFlashAttribute("successMessage", "Đã theo dõi người dùng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/unfollow/{userId}")
    public String unfollowUser(@PathVariable Long userId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userService.findByUsername(principal.getName());
            User userToUnfollow = userService.findById(userId);
            followService.unfollowUser(currentUser, userToUnfollow);
            redirectAttributes.addFlashAttribute("successMessage", "Đã bỏ theo dõi người dùng thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/users";
    }
}