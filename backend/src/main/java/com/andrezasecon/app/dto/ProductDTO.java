package com.andrezasecon.app.dto;

import com.andrezasecon.app.entities.Category;

import java.io.Serializable;

public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductDTO(Category entity){
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
