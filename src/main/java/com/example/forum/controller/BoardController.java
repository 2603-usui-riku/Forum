package com.example.forum.controller;

import com.example.forum.dto.request.TopicRequest;
import com.example.forum.dto.response.BoardDetailResponse;
import com.example.forum.dto.response.BoardResponse;
import com.example.forum.service.BoardService;
import com.example.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private TopicService topicService;

    @GetMapping("/")
    public String index(Model model) {
        List<BoardResponse> boards = boardService.findTopPageBoards();

        model.addAttribute("boards", boards);
        return "board/index";
    }

    @GetMapping("/{slug}")
    public String show(
            @PathVariable String slug,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        TopicRequest request = new TopicRequest();

        BoardResponse board = boardService.findBoardBySlug(slug);
        BoardDetailResponse data = topicService.findBoardDetail(slug, page, 20);

        model.addAttribute("topicRequest", request);
        model.addAttribute("board", board);
        model.addAttribute("hotTopics", data.getHotTopics());
        model.addAttribute("newTopics", data.getNewTopics());
        model.addAttribute("topicPage", data.getMainTopicPage());
        return "board/show";
    }
}
