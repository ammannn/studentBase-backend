package com.university.mcmaster.exceptions;

public class UnAuthenticatedUserException extends RuntimeException{
    public UnAuthenticatedUserException(){
        super("user authentication is required to perform the task");
    }
}
