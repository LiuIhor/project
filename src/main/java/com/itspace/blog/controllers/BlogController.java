package com.itspace.blog.controllers;

import com.itspace.blog.models.Post;
import com.itspace.blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return "blog-add";
    }

    @PostMapping("blog/add")
    public String blogAddNew(@RequestParam String title,
            @RequestParam String anons,
            @RequestParam String full_text,
                             Model model){
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public  String blogDetails(@PathVariable(value = "id") long id, Model model){
        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        int views = res.get(0).getViews()+1;
        res.get(0).setViews(views);
        postRepository.save(res.get(0));
        model.addAttribute("post", res);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") Long id,
                           Model model){
        if(!postRepository.existsById(id)){
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-edit";
    }

    @PostMapping("blog/{id}/edit")
    public String blogUpdate(@PathVariable(value = "id") long id,
                             @RequestParam String title,
                             @RequestParam String anons,
                             @RequestParam String full_text,
                             Model model) {

        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        res.get(0).setTitle(title);
        res.get(0).setAnons(anons);
        res.get(0).setFull_text(full_text);
        postRepository.save(res.get(0));
        return "redirect:/blog";
    }
    @PostMapping("/blog/{id}/remove")
    public String blogDellete(@PathVariable(value = "id") long id,
                             Model model) {

        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        postRepository.delete(res.get(0));
        return "redirect:/blog";
    }

    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }
}
