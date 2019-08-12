package com.endava.homework.springredis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@RedisHash("Product")
public class Product implements Serializable {
    @Id
    private Long id;
    private String category;
    private String subcategory;
    private String name;
    private String description;
    private List<String> tags;

    public Product() {

    }

    public Product(Long id, String category, String subcategory, String name, String description, List<String> tags) {
        this.id = id;
        this.category = category;
        this.subcategory = subcategory;
        this.name = name;
        this.description = description;
        this.tags = tags;
    }
}
