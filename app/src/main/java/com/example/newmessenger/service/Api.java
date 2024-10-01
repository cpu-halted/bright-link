package com.example.newmessenger.service;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;

public class Api {
    private static Api api;
    private final String PROTOCOL = "http";
    private final String DOMAIN = "185.221.214.190";
    private final Integer PORT = 8000;

    public static Api getInstance() {
        if (api == null) {
            api = new Api();
        }

        return api;
    }

    public void login(String email, String password) {
        try {
            sendRequest(
                    Request.Method.POST,
                    "/api/login",
                    new JSONObject().put("email", email).put("password", password),
                    (Response.Listener<JSONObject>) response -> {
                        Log.i("Api", response.toString());
                    });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendRequest(int method, String endpoint, JSONObject data, Response.Listener<JSONObject> responseListener) {
        try {
            URL url = new URL(PROTOCOL, DOMAIN, PORT, endpoint);
            Log.i("Api", "Sending request: " + url.toString());
            JsonObjectRequest request = new JsonObjectRequest(
                    method,
                    url.toString(),
                    data,
                    responseListener,
                    (Response.ErrorListener) error -> {
                        Log.e("Api", error.toString());
                    });
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
