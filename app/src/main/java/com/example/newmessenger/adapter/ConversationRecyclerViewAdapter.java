package com.example.newmessenger.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newmessenger.Api;
import com.example.newmessenger.Auth;
import com.example.newmessenger.R;
import com.example.newmessenger.listener.OnSwipeTouchListener;
import com.example.newmessenger.model.Conversation;
import com.example.newmessenger.model.Message;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversationRecyclerViewAdapter extends RecyclerView.Adapter<ConversationRecyclerViewAdapter.ViewHolder> {

    private List<Conversation> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    public ConversationRecyclerViewAdapter(Context context, List<Conversation> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.conversation_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Conversation conversation = mData.get(position);
        holder.titleTextView.setText(conversation.getTitle());
        holder.descriptionTextView.setText(conversation.getDescription());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView descriptionTextView;


        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.conversationTitle);
            descriptionTextView = itemView.findViewById(R.id.conversationDescription);
            itemView.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public boolean onClick() {
                    mClickListener.onItemClick(getAdapterPosition());
                    return true;
                }

                @Override
                public void onSwipeLeft() {
                    Conversation conversation = mData.get(getAdapterPosition());
                    Toast.makeText(context, "Swiped " + conversation.getId() + " conv", Toast.LENGTH_SHORT).show();
                    Api.getInstance().getApiService().leave(Auth.getInstance().getToken(), conversation.getId()).enqueue(
                            new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.i("Api", "Leave " + conversation.getId() + " coversation");
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("Api", "Failed to leave conversation");
                                    call.clone().enqueue(this);
                                }
                            }
                    );
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }
    }

    public Conversation getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}