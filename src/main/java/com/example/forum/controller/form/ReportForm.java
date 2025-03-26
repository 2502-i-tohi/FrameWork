package com.example.forum.controller.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportForm {
    private int id;

    @NotEmpty(message = "投稿内容を入力してください")
    private String content;

    private LocalDateTime createdAt;
}