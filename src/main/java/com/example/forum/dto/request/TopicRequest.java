package com.example.forum.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TopicRequest {
    @NotBlank(message = "スレッドのタイトルを入力してください")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください")
    private String title;

    @NotBlank(message = "最初の本文を入力してください")
    @Size(max = 1000, message = "本文は1000文字以内で入力してください")
    private String content;
}
