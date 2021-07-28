package com.jodo.android.model;

public class User {
    Integer id;
    String username,password,shop_name,address,contact;

    public User() {
    }

    public User(Integer id,String username, String password, String shop_name, String address, String contact) {
        this.username = username;
        this.password = password;
        this.shop_name = shop_name;
        this.address = address;
        this.contact = contact;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
