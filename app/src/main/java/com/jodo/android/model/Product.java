package com.jodo.android.model;

public class Product {
    Integer id,price;
    String name,product_id;

    public Product() {
    }

    public Product(Integer id, Integer price, String product_id, String name) {
        this.id = id;
        this.price = price;
        this.product_id = product_id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
