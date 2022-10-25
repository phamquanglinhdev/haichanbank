package com.adonis.haichanbank.dto;

public class BusinessDto {
    private String name;
    private String token;
    private String callback;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public BusinessDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "BusinessDto{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}