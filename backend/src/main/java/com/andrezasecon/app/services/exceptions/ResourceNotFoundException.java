package com.andrezasecon.app.services.exceptions;

import javax.persistence.Entity;

public class ResourceNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String msg){
        super(msg);
    }
}
