package com.truong.springboot.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.truong.springboot.service.PostService;
import com.truong.springboot.service.UserService;
import com.truong.springboot.entity.Post;
import com.truong.springboot.entity.User;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listPost(Model model)
    {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/create")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post_form";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/logon";
        }
        User user = userService.findByUsername(principal.getName());
        post.setUser(user);
        postService.savePost(post);
        redirectAttributes.addFlashAttribute("successMessage", "Post created successfully");
        return "redirect:/post";
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "post_detail";
    }

    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model, Principal principal) {
        Post post = postService.getPostById(id);
        User user = userService.findByUsername(principal.getName());
        if (!post.getUser().getId().equals(user.getId())) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post);
        return "post_edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, Principal principal, RedirectAttributes redirectAttributes) {
        Post existingPost = postService.getPostById(id);
        User user = userService.findByUsername(principal.getName());
        if (!existingPost.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to edit this post");
            return "redirect:/posts";
        }
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setStatus(post.getStatus());
        postService.savePost(existingPost);
        redirectAttributes.addFlashAttribute("successMessage", "Post updated successfully");
        return "redirect:/posts";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Post post = postService.getPostById(id);
        User user = userService.findByUsername(principal.getName());
        if (!post.getUser().getId().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You do not have permission to delete this post");
            return "redirect:/posts";
        }
        postService.deletePost(id);
        redirectAttributes.addFlashAttribute("successMessage", "Post deleted successfully");
        return "redirect:/posts";
    }
}