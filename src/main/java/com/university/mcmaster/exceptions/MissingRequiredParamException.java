package com.university.mcmaster.exceptions;

public class MissingRequiredParamException extends RuntimeException {
    public MissingRequiredParamException(){
        super("please provide missing required params");
    }

    public MissingRequiredParamException(String param){
        super("please provide missing required param : " + param);
    }
}
