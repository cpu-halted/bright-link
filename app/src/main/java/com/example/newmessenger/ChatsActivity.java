package com.example.newmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newmessenger.adapter.ConversationRecyclerViewAdapter;
import com.example.newmessenger.model.Conversation;
import com.example.newmessenger.model.Message;
import com.example.newmessenger.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsActivity extends AppCompatActivity implements ConversationRecyclerViewAdapter.ItemClickListener  {

    ConversationRecyclerViewAdapter adapter;
    List<Conversation> chats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getSupportActionBar().setTitle("Чаты");

        RecyclerView chatsRecyclerView = findViewById(R.id.chat_list);
        chatsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chats = new ArrayList<>();
        adapter = new ConversationRecyclerViewAdapter(this, chats);
        adapter.setClickListener(this);
        chatsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ChatsActivity.this, ChatActivity.class);
        intent.putExtra("id", adapter.getItem(position).getId());
        intent.putExtra("title", adapter.getItem(position).getTitle());
        startActivity(intent);
    }

    private boolean isUpdating;
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
        Api.getInstance().getApiService().conversations(Auth.getInstance().getToken()).enqueue(
                new Callback<List<Conversation>>() {
                    @Override
                    public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                        List<Conversation> conversations = response.body();
                        Log.i("Api", "Received " + conversations.size() + " chats");

                        chats.clear();
                        adapter.notifyDataSetChanged();

                        for (Conversation conversation : conversations) {
                            chats.add(conversation);
                        }

                        adapter.notifyDataSetChanged();
                        isUpdating = false;
                    }

                    @Override
                    public void onFailure(Call<List<Conversation>> call, Throwable t) {
                        Log.e("Api", "failed to get chats");
                        call.clone().enqueue(this);
                    }
                }
        );
    }
}