package com.example.forum.dto.response;

import com.example.forum.entity.Topic;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class BoardDetailResponse {
    private List<TopicResponse> hotTopics;
    private List<TopicResponse> newTopics;
    private Page<TopicResponse> mainTopicPage;
}
