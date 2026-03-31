package com.example.forum.controller;

import com.example.forum.dto.request.PostRequest;
import com.example.forum.dto.request.TopicRequest;
import com.example.forum.dto.response.PostResponse;
import com.example.forum.dto.response.TopicResponse;
import com.example.forum.entity.Topic;
import com.example.forum.service.PostService;
import com.example.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("{slug}/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;
    @Autowired
    private PostService postService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping("/{topicId}")
    public String show(
            @PathVariable String slug,
            @PathVariable Integer topicId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ) {
        PostRequest request = new PostRequest();
        Page<PostResponse> postPage = postService.findPostsByTopicId(topicId, page, 100);

        TopicResponse topic = topicService.findTopicDetail(slug, topicId);

        model.addAttribute("postRequest", request);
        model.addAttribute("postPage", postPage);
        model.addAttribute("topic", topic);
        model.addAttribute("slug", slug);
        model.addAttribute("topicId", topicId);
        return "topic/show";
    }

    @PostMapping("/add")
    public String addTopic(
            @PathVariable String slug,
            @ModelAttribute("formModel") TopicRequest req,
            RedirectAttributes redirectAttributes
    ) {
        Integer topicId = topicService.createTopic(slug, req);

        redirectAttributes.addAttribute("slug", slug);
        redirectAttributes.addAttribute("topicId", topicId);
        return "redirect:/{slug}/topic/{topicId}";
    }

    @PutMapping("/{topicId}/edit")
    public String editTopic(
            @PathVariable String slug,
            @PathVariable Integer topicId,
            @ModelAttribute("formModel") TopicRequest req,
            RedirectAttributes redirectAttributes
    ) {
        topicService.editTopic(topicId, req);

        redirectAttributes.addAttribute("slug", slug);
        redirectAttributes.addAttribute("topicId", topicId);
        return "redirect:/{slug}/topic/{topicId}";
    }

    @DeleteMapping("/{topicId}/delete")
    public String deleteTopic(
            @PathVariable String slug,
            @PathVariable Integer topicId,
            RedirectAttributes redirectAttributes
    ) {
        topicService.deleteTopic(topicId);

        redirectAttributes.addAttribute("slug", slug);
        return "redirect:/{slug}";
    }
}
