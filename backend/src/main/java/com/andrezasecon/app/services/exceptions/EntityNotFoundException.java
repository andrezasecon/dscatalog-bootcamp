package com.andrezasecon.app.services.exceptions;

import javax.persistence.Entity;

public class EntityNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String msg){
        super(msg);
    }
}
