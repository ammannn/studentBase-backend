package com.university.mcmaster.exceptions;

public class InvalidParamValueException extends RuntimeException {
    public InvalidParamValueException(){
        super("invalid value given for request param");
    }
    public InvalidParamValueException(String param, String allowedValues) {
        super("invalid value given for request param : " + param + " , allowed values are : " + allowedValues);
    }

    public InvalidParamValueException(String param) {
        super("invalid value given for request param : " + param);
    }
}
