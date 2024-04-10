package com.university.mcmaster.exceptions;

import lombok.Getter;

@Getter
public class ActionNotAllowedException extends RuntimeException {
    private int code = 400;
    public ActionNotAllowedException(){
        super("user is not allowed to perform action");

    }
    public ActionNotAllowedException(String action,String reason,int code){
        super("user is not allowed to perform action : " +action + ", reason: " +reason);
        this.code = code;
    }
}
