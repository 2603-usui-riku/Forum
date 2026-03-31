package com.example.forum.service;

import com.example.forum.dto.response.BoardResponse;
import com.example.forum.dto.response.PostResponse;
import com.example.forum.entity.Post;
import com.example.forum.repository.BoardRepository;
import com.example.forum.repository.TopicRepository;
import com.example.forum.entity.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TopicService topicService;

    /*
     * 板の全件取得処理
     */
    public List<BoardResponse> findAllBoard() {
        List<Board> results = boardRepository.findAll();
        return toResponseList(results);
    }

    /*
     * 板と板のスレッド5件取得処理
     */
    public List<BoardResponse> findTopPageBoards() {
        List<Board> boards = boardRepository.findAll();

        return boards.stream().map(board -> {
            BoardResponse res = convertToResponse(board);
            res.setTopTopics(topicService.findTop5Topics(board.getSlug()));

            return res;
        }).toList();
    }

    /*
     * 特定の板取得処理
     */
    public BoardResponse findBoardBySlug(String slug) {
        Board board = boardRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("板が見つかりません"));
        return convertToResponse(board);
    }

    /*
     * 画面表示用にリスト化
     */
    private List<BoardResponse> toResponseList(List<Board> boards) {
        return boards.stream().map(this::convertToResponse).toList();
    }

    /*
     * 内容を一件ずつレスポンス用に調整
     */
    private BoardResponse convertToResponse(Board board) {
        BoardResponse response = new BoardResponse();
        response.setId(board.getId());
        response.setName(board.getName());
        response.setSlug(board.getSlug());
        response.setDescription(board.getDescription());

        return response;
    }
}
