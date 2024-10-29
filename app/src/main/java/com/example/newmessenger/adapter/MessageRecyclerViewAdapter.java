package com.example.newmessenger.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newmessenger.Auth;
import com.example.newmessenger.R;
import com.example.newmessenger.listener.OnSwipeTouchListener;
import com.example.newmessenger.model.Message;
import com.example.newmessenger.model.Token;
import com.example.newmessenger.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder> {

    private List<Message> mData;
    private List<User> users = new ArrayList<>();

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private OnDeleteMessageListener onDeleteMessageListener;
    private Message selectedMessage;
    private int selectedMessageId;


    public interface OnDeleteMessageListener {
        void onDeleteMessage(Message message);
    }

    // data is passed into the constructor
    public MessageRecyclerViewAdapter(Context context, List<Message> data, OnDeleteMessageListener onDeleteMessageListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.onDeleteMessageListener = onDeleteMessageListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mData.get(position);

        String userName = null;

        for (User user: users) {
            if (user.getId() == message.getSenderId()) {
                userName = user.getName();
                break;
            }
        }


        if (message.getSenderId() == Auth.getInstance().getUserId()) {
            holder.messageLayoutLeft.setVisibility(View.GONE);
            holder.messageLayoutRight.setVisibility(View.VISIBLE);
            holder.messageTextviewRight.setText(message.getContent());
            holder.authorTextViewRight.setText(userName);
        }
        else {
            holder.messageLayoutRight.setVisibility(View.GONE);
            holder.messageLayoutLeft.setVisibility(View.VISIBLE);
            holder.messageTextviewLeft.setText(message.getContent());
            holder.authorTextViewLeft.setText(userName);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout messageLayoutLeft;
        LinearLayout messageLayoutRight;
        TextView messageTextviewLeft;
        TextView messageTextviewRight;
        TextView authorTextViewRight;
        TextView authorTextViewLeft;

        ViewHolder(View itemView) {
            super(itemView);
            messageLayoutLeft = itemView.findViewById(R.id.message_layout_left);
            messageLayoutRight = itemView.findViewById(R.id.message_layout_right);
            messageTextviewLeft = itemView.findViewById(R.id.message_textview_left);
            messageTextviewRight = itemView.findViewById(R.id.message_textview_right);
            authorTextViewRight = itemView.findViewById(R.id.author_textview_right);
            authorTextViewLeft = itemView.findViewById(R.id.author_textview_left);

            itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public void onLongClick() {
                    Message message = mData.get(getAdapterPosition());
                    if (message.getSenderId().equals(Auth.getInstance().getUserId())) {
                        showDeleteConfirmationDialog(message);
                    }
                }
            });
        }
    }

    public Message getItem(int id) {
        return mData.get(id);
    }

    private void showDeleteConfirmationDialog(Message message) {
        new AlertDialog.Builder(context)
                .setTitle("Удалить сообщение?")
                .setMessage("Вы уверены, что хотите удалить это сообщение?")
                .setPositiveButton("Да", (dialog, which) -> {
                    onDeleteMessageListener.onDeleteMessage(message);
                })
                .setNegativeButton("Нет", null)
                .show();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}