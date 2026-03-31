package com.example.forum.repository;

import com.example.forum.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    // 人気の5件（返信数順）
    List<Topic> findTop5ByBoardSlugOrderByPostCountDesc(String slug);
    // 最新の5件（作成日時順）
    List<Topic> findTop5ByBoardSlugOrderByCreatedAtDesc(String slug);
    // メインの一覧（ページネーション用）
    Page<Topic> findByBoardSlugOrderByCreatedAtDesc(String slug, Pageable pageable);
    // 一件
    Optional<Topic> findByIdAndBoardSlug(Integer id, String slug);
}
