package com.inn.cafe.bean;

import lombok.Data;

@Data
public class ProductBean {
    Integer id;
    String name;
    String description;
    Integer price;
    String status;
    Integer categoryId;
    String categoryName;

    public ProductBean(){

    }

    public ProductBean(Integer id, String name, String description, Integer price, String status, Integer categoryId, String categoryName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ProductBean(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public ProductBean(Integer id, String name, String description, Integer price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
