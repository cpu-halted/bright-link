package com.example.newmessenger.model;

public class RegisterRequest {
    final String email;
    final String password;
    final String name;
    final String phone;

    public RegisterRequest(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }
}
