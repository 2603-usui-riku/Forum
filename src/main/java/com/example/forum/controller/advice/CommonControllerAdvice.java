package com.example.forum.controller.advice;

import com.example.forum.dto.response.BoardResponse;
import com.example.forum.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class CommonControllerAdvice {
    @Autowired
    private BoardService boardService;

    @ModelAttribute("boards")
    public List<BoardResponse> addCommonBoards() {
        return boardService.findAllBoard();
    }
}
