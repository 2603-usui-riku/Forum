package com.example.forum.service;

import com.example.forum.dto.request.PostRequest;
import com.example.forum.dto.request.TopicRequest;
import com.example.forum.dto.response.BoardDetailResponse;
import com.example.forum.dto.response.TopicResponse;
import com.example.forum.entity.Post;
import com.example.forum.repository.BoardRepository;
import com.example.forum.repository.TopicRepository;
import com.example.forum.entity.Board;
import com.example.forum.entity.Topic;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private PostService postService;

    /*
     * スレッド一覧取得処理
     */
    public Page<TopicResponse> findTopicsByBoardSlug(String slug, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Topic> results = topicRepository.findByBoardSlugOrderByCreatedAtDesc(slug, pageable);

        return results.map(this::convertToResponse);
    }

    /*
     * 特定のスレッド取得処理
     */
    public TopicResponse findTopicDetail(String slug, Integer topicId) {
        Topic topic = topicRepository.findByIdAndBoardSlug(topicId, slug).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "スレッドが見つかりません"));

        return convertToResponse(topic);
    }

    /*
     * ３種のスレッド一覧取得処理（ページネーション）
     */
    public BoardDetailResponse findBoardDetail(String slug, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        BoardDetailResponse res = new BoardDetailResponse();

        // それぞれ独立して取得
        res.setHotTopics(toResponseList(topicRepository.findTop5ByBoardSlugOrderByPostCountDesc(slug)));
        res.setNewTopics(toResponseList(topicRepository.findTop5ByBoardSlugOrderByCreatedAtDesc(slug)));
        res.setMainTopicPage(topicRepository.findByBoardSlugOrderByCreatedAtDesc(slug, pageable).map(this::convertToResponse));

        return res;
    }

    /*
     * 投稿数の多いスレッドトップ5件取得処理
     */
    public List<TopicResponse> findTop5Topics(String slug) {
        List<Topic> topics = topicRepository.findTop5ByBoardSlugOrderByPostCountDesc(slug);

        return toResponseList(topics);
    }

    /*
     * 新規スレッド作成処理
     */
    @Transactional
    public Integer createTopic(String slug, TopicRequest topicRequest) {
        Board board = boardRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("板が見つかりません"));


        Topic topic = new Topic();
        topic.setTitle(topicRequest.getTitle());
        topic.setBoard(board);
        topic.setPostCount(0);
        topic.setLastPostAt(LocalDateTime.now());

        Topic savedTopic = topicRepository.save(topic);

        postService.addPostToTopic(savedTopic, topicRequest.getContent());

        return topic.getId();
    }

    /*
     * スレッド編集処理
     */
    public void editTopic(Integer id, TopicRequest request) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new RuntimeException("スレッドが存在しません"));

        topic.setTitle(request.getTitle());
        topicRepository.save(topic);
    }

    /*
     * スレッド削除処理
     */
    @Transactional
    public void deleteTopic(Integer id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new RuntimeException("スレッドが存在しません"));

        topicRepository.delete(topic);
    }

    /*
     * 画面表示用にリスト化
     */
    private List<TopicResponse> toResponseList(List<Topic> topics) {
        return topics.stream().map(this::convertToResponse).toList();
    }

    /*
     * 内容を一件ずつレスポンス用に調整
     */
    private TopicResponse convertToResponse(Topic topic) {
        TopicResponse res = new TopicResponse();
        res.setId(topic.getId());
        res.setBoardSlug(topic.getBoard().getSlug());
        res.setTitle(topic.getTitle());
        res.setPostCount(topic.getPostCount());

        if(topic.getLastPostAt() != null)
            res.setLastPostAtStr(topic.getLastPostAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));

        return res;
    }
}
