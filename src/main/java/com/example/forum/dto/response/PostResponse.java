package com.example.forum.dto.response;

import lombok.Data;

@Data
public class PostResponse {
    private Integer id;
    private Integer postNum;
    private String content;
    private Boolean isEdited;
    private Boolean isDeleted;
    private String createdAtStr;
}
