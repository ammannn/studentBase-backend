package com.university.mcmaster.exceptions;

public class FailedToPerformOperation extends RuntimeException {
    public FailedToPerformOperation(){
        super("failed to perform action");
    }
}
