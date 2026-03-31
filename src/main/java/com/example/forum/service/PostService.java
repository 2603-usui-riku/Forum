package com.example.forum.service;

import com.example.forum.dto.request.PostRequest;
import com.example.forum.dto.response.PostResponse;
import com.example.forum.repository.PostRepository;
import com.example.forum.repository.TopicRepository;
import com.example.forum.entity.Post;
import com.example.forum.entity.Topic;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TopicRepository topicRepository;

    /*
     * 特定のスレッドの全投稿取得処理
     */
    public Page<PostResponse> findPostsByTopicId(Integer topicId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postNum").descending());

        Page<Post> results = postRepository.findByTopicIdOrderByPostNumDesc(topicId, pageable);

        return results.map(this::convertToResponse);
    }

    /*
     * 特定の投稿取得処理
     */
    public PostResponse findPostById(Integer postId) {
        List<Post> results = new ArrayList<>();
        results.add(postRepository.findById(postId).orElse(null));

        return convertToResponse(results.get(0));
    }

    /*
     * 新規投稿登録処理
     */
    @Transactional
    public void addReply(Integer topicId, PostRequest req) {
        Topic topic = topicRepository.findById(topicId).orElseThrow();

        addPostToTopic(topic, req.getContent());
    }

    /*
     * 投稿編集処理
     */
    public void editPost(Integer id, PostRequest request) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("投稿が存在しません"));

        post.setContent(request.getContent());
        post.setIsEdited(true);

        postRepository.save(post);
    }

    /*
     * 投稿削除処理
     */
    public void deletePost(Integer id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("投稿が存在しません"));
        post.setIsDeleted(true);

        postRepository.save(post);
    }

    /*
     * スレッドに投稿を追加する処理
     */
    @Transactional
    public void addPostToTopic(Topic topic, String content) {
        int nextPostNum = topic.getPostCount() + 1;

        Post post = new Post();
        post.setTopic(topic);
        post.setPostNum(nextPostNum);
        post.setContent(content);
        post.setIsEdited(false);
        post.setIsDeleted(false);

        topic.setPostCount(nextPostNum);
        topic.setLastPostAt(LocalDateTime.now());

        postRepository.save(post);
    }

    /*
     * 画面表示用にリスト化
     */
    private List<PostResponse> toResponseList(List<Post> posts) {
        return posts.stream().map(this::convertToResponse).toList();
    }

    /*
     * 内容を一件ずつレスポンス用に調整
     */
    private PostResponse convertToResponse(Post post) {
        PostResponse res = new PostResponse();
        res.setId(post.getId());
        res.setPostNum(post.getPostNum());
        res.setContent(post.getContent());
        res.setIsEdited(post.getIsEdited());
        res.setIsDeleted(post.getIsDeleted());

        if (post.getCreatedAt() != null)
            res.setCreatedAtStr(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));

        return res;
    }
}
