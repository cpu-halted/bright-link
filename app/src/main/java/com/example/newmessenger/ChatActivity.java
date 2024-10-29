package com.example.newmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newmessenger.adapter.ConversationRecyclerViewAdapter;
import com.example.newmessenger.adapter.MessageRecyclerViewAdapter;
import com.example.newmessenger.model.Conversation;
import com.example.newmessenger.model.Message;
import com.example.newmessenger.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements MessageRecyclerViewAdapter.OnDeleteMessageListener  {

    MessageRecyclerViewAdapter adapter;
    Long chatId;
    List<Message> messages;
    Boolean firstTime;
    RecyclerView messagesRecyclerView;
    private boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().setTitle(extras.getString("title"));

        Button send = findViewById(R.id.send_button);
        EditText input = findViewById(R.id.message_input);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(input.getText().toString());
                input.getText().clear();
            }
        });

        messagesRecyclerView = findViewById(R.id.message_recycler_view);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messages = new ArrayList<>();
        adapter = new MessageRecyclerViewAdapter(this, messages, this);
        messagesRecyclerView.setAdapter(adapter);

        chatId = extras.getLong("id");
        firstTime = true;

    }

    public void sendMessage(String text) {
        Message message = new Message(0L, "text", text, null, null);
        Api.getInstance().getApiService().sendMessage(Auth.getInstance().getToken(), chatId, message).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i("Api", "Sent message");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Api", "Failed to send message");
                        call.clone().enqueue(this);
                    }
                }
        );
    }

    private Handler handler = new Handler();
    private Runnable updateMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isUpdating) {
                updateMessages();
            }

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(updateMessagesRunnable, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(updateMessagesRunnable);
    }

    public void updateMessages() {
        isUpdating = true;

        Call<List<User>> usersRequest = Api.getInstance().getApiService().getConversationUsers(Auth.getInstance().getToken(), chatId);
        usersRequest.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                adapter.setUsers(response.body());

                Api.getInstance().getApiService().getMessages(Auth.getInstance().getToken(), chatId).enqueue(
                        new Callback<List<Message>>() {
                            @Override
                            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                                List<Message> received = response.body();
                                Log.i("Api", "Received " + received.size() + " messages");

                                messages.clear();
                                adapter.notifyDataSetChanged();

                                for (Message message : received) {
                                    messages.add(message);
                                }
                                adapter.notifyDataSetChanged();

                                if (firstTime) {
                                    messagesRecyclerView.scrollToPosition(messages.size() - 1);
                                    firstTime = false;
                                }

                                isUpdating = false;
                            }

                            @Override
                            public void onFailure(Call<List<Message>> call, Throwable t) {
                                Log.e("Api", "Failed to get messages");
                                call.clone().enqueue(this);
                            }
                        }
                );
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Api", "Failed to get users");
                call.clone().enqueue(this);
            }
        });
    }

    @Override
    public void onDeleteMessage(Message message) {
        Api.getInstance().getApiService().deleteMessage(Auth.getInstance().getToken(), chatId, message.getId()).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i("Api", "Deleted " + message.getId() + " message");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Api", "Failed to delete message");
                        call.clone().enqueue(this);
                    }
                }
        );
    }
}