package com.example.forum.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BoardResponse {
    private Integer id;
    private String name;
    private String slug;
    private String description;

    private List<TopicResponse> topTopics;
}