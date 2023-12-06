package com.example.management_task.exceprtions;

public class TaskException extends RuntimeException {
    public TaskException(String msg){
        super(msg);
    }
    public TaskException(Long id,String email){
        super(String.format("task is not exist by id : %s and userEmail : %s",id,email));
    }
}
