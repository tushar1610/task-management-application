package com.example.task.management.application.error.exception;


public class InternalServerException extends RuntimeException{

    public InternalServerException(String message){
        super(message);
    }
}
