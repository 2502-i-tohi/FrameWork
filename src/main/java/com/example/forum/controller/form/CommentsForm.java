package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentsForm {
    private int id;
    private String text;
    private int contentId;
}
