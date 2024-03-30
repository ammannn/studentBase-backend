package com.university.mcmaster.exceptions;

public class ActionNotAllowedException extends RuntimeException {
    public ActionNotAllowedException(){
        super("user is not allowed to perform action");

    }
    public ActionNotAllowedException(String action,String reason){
        super("user is not allowed to perform action : " +action + ", reason: " +reason);
    }
}
