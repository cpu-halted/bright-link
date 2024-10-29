package com.example.newmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newmessenger.model.LoginRequest;
import com.example.newmessenger.model.RegisterRequest;
import com.example.newmessenger.model.Token;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.registerButton);
        EditText email = findViewById(R.id.emailText);
        EditText password = findViewById(R.id.passwordText);
        EditText name = findViewById(R.id.phone);
        EditText phone = findViewById(R.id.name);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance().getApiService().register(
                        new RegisterRequest(
                                email.getText().toString(),
                                password.getText().toString(),
                                name.getText().toString(),
                                phone.getText().toString()
                        )
                ).enqueue(new Callback<ResponseBody>() {
                              @Override
                              public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                  try {
                                      if (response.code() != 200) {
                                          Toast.makeText(RegisterActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                                      }
                                      else {
                                          Toast.makeText(RegisterActivity.this, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                                          leave();
                                      }
                                      Log.i("Api", response.body().string());
                                  } catch (IOException e) {
                                      throw new RuntimeException(e);
                                  }
                              }

                              @Override
                              public void onFailure(Call<ResponseBody> call, Throwable t) {

                              }
                          }
                );
            }
        });
    }

    private void leave() {
        finish();
    }
}