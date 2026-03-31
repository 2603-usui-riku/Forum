package com.example.forum.repository;

import com.example.forum.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // 一覧（ページネーション）
    Page<Post> findByTopicIdOrderByPostNumDesc(Integer topicId, Pageable pageable);
}

