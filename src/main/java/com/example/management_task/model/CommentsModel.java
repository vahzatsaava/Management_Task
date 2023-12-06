package com.example.management_task.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsModel {

    private Long id;

    private String text;

    private LocalDateTime created;

    private Long author;
}
