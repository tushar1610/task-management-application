package com.example.task.management.application.error.exception;

public class UserNotAuthorizedException extends RuntimeException{

    public UserNotAuthorizedException(String message){
        super(message);
    }
}
