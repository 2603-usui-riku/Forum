package com.example.forum.controller;

import com.example.forum.dto.response.PostResponse;
import com.example.forum.dto.response.TopicResponse;
import com.example.forum.service.TopicService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import com.example.forum.dto.request.PostRequest;
import com.example.forum.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("{slug}/topic/{topicId}")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private TopicService topicService;

    /*
     * 投稿編集画面表示
     */
    @GetMapping("/{id}/edit")
    public String getEditPost(
            @PathVariable String slug,
            @PathVariable Integer topicId,
            @PathVariable Integer id,
            Model model
    ) {
        PostRequest request = new PostRequest();
        PostResponse post = postService.findPostById(id);
        TopicResponse topic = topicService.findTopicDetail(slug, topicId);

        model.addAttribute("postRequest", request);
        model.addAttribute("post", post);
        model.addAttribute("slug", slug);
        model.addAttribute("topic", topic);

        return "post/edit";
    }

    /*
     * 投稿削除確認画面表示
     */
    @GetMapping("/{id}/delete")
    public String getDeletePost(
            @PathVariable String slug,
            @PathVariable Integer topicId,
            @PathVariable Integer id,
            Model model
    ) {
        PostResponse post = postService.findPostById(id);

        model.addAttribute("post", post);
        model.addAttribute("slug", slug);
        model.addAttribute("topicId", topicId);

        return "post/delete";
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public String addPost(
            @PathVariable String slug,
            @PathVariable Integer topicId,
            @ModelAttribute("formModel") PostRequest req,
            RedirectAttributes redirectAttributes
    ) {
        postService.addReply(topicId, req);

        redirectAttributes.addAttribute("slug", slug);
        redirectAttributes.addAttribute("topicId", topicId);
        return "redirect:/{slug}/topic/{topicId}";
    }

    /*
     * 投稿編集処理
     */
    @PutMapping("/{id}/edit")
    public String editContent(
            @PathVariable String slug,
            @PathVariable Integer topicId,
            @PathVariable Integer id,
            @ModelAttribute("formModel") PostRequest req,
            RedirectAttributes redirectAttributes
    ) {
        postService.editPost(id, req);

        redirectAttributes.addAttribute("slug", slug);
        redirectAttributes.addAttribute("topicId", topicId);
        return "redirect:/{slug}/topic/{topicId}";
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/{id}/delete")
    public String deletePost(
            @PathVariable String slug,
            @PathVariable Integer topicId,
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes
    ) {
        postService.deletePost(id);

        redirectAttributes.addAttribute("slug", slug);
        redirectAttributes.addAttribute("topicId", topicId);
        return "redirect:/{slug}/topic/{topicId}";
    }
}

