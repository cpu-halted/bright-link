package com.example.newmessenger.model;

import com.google.gson.annotations.SerializedName;

public class User {
    final Long id;
    final String name;
    final String phone;
    final String email;

    public User(Long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
