package com.example.newmessenger;

public class Auth {
    private String token;
    private Long userId;
    private static Auth instance;

    private Auth() {

    }

    public static Auth getInstance() {
        if (instance == null) {
            instance = new Auth();
        }

        return instance;
    }

    public boolean isTokenSet() {
        return !token.isEmpty();
    }

    public String getToken() {
        return "Bearer " + token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        userId = id;
    }

}
