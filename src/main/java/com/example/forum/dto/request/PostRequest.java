package com.example.forum.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message="本文を入力したください")
    @Size(max = 1000, message="本文は1000字以内で入力してください")
    private String content;
}
