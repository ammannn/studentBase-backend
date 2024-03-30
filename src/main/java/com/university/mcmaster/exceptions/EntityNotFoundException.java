package com.university.mcmaster.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(){
        super("no entity found for given id");
    }

    public EntityNotFoundException(String entity,String id){
        super("no "+entity+" found for give id : " + id);
    }
}
