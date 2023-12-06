package com.example.management_task.exceprtions;

public class CommentsExistException extends RuntimeException {
    public CommentsExistException(String msg) {
        super(msg);
    }
}
