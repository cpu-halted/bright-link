package com.example.newmessenger.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    final String email;
    final String password;

    @SerializedName("device_id")
    final String deviceId;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.deviceId = "Mobile";
    }
}
