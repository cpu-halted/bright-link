package com.example.newmessenger;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newmessenger.service.Api;

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


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Api api = Api.getInstance();
                api.login(email.toString(), password.toString());
            }
        });
    }
}