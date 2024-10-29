package com.example.newmessenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newmessenger.model.LoginRequest;
import com.example.newmessenger.model.Token;
import com.example.newmessenger.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login);

        Button enterButton = findViewById(R.id.enterButton);
        Button registerButton = findViewById(R.id.registerButton);
        EditText email = findViewById(R.id.emailText);
        EditText password = findViewById(R.id.passwordText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api.getInstance().getApiService().login(
                        new LoginRequest(email.getText().toString(), password.getText().toString())
                ).enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (response.code() != 200) {
                                Toast.makeText(LoginActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            applyLogin(response.body());
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {

                        }
                    }
                );
            }
        });
    }

    public void applyLogin(Token token) {
        Api.getInstance().getApiService().me(
                "Bearer " + token.getToken()
        ).enqueue(new Callback<User>() {
                      @Override
                      public void onResponse(Call<User> call, Response<User> response) {
                          if (response.code() != 200) {
                              Toast.makeText(LoginActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                              return;
                          }

                          Auth.getInstance().setToken(token.getToken());
                          Auth.getInstance().setUserId(response.body().getId());
                          Intent intent = new Intent(LoginActivity.this, ChatsActivity.class);
                          startActivity(intent);
                      }

                      @Override
                      public void onFailure(Call<User> call, Throwable t) {
                          Log.e("Api", "failed to log in");
                          call.clone().enqueue(this);
                      }
                  }
        );
    }
}