package com.example.forum.dto.response;

import lombok.Data;

@Data
public class TopicResponse {
    private Integer id;
    private String boardSlug;
    private String title;
    private Integer postCount;
    private String lastPostAtStr;
}
