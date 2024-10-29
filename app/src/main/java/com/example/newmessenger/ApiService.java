package com.example.newmessenger;

import com.example.newmessenger.model.Conversation;
import com.example.newmessenger.model.LoginRequest;
import com.example.newmessenger.model.Message;
import com.example.newmessenger.model.RegisterRequest;
import com.example.newmessenger.model.Token;
import com.example.newmessenger.model.User;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/login")
    Call<Token> login(@Body LoginRequest body);
    @POST("api/register")
    Call<ResponseBody> register(@Body RegisterRequest body);
    @GET("api/me")
    Call<User> me(@Header("Authorization") String token);
    @GET("api/conversation")
    Call<List<Conversation>> conversations(@Header("Authorization") String token);
    @GET("api/conversation/{chatId}/message")
    Call<List<Message>> getMessages(@Header("Authorization") String token, @Path("chatId") Long chatId);
    @POST("api/conversation/{chatId}/message")
    Call<ResponseBody> sendMessage(@Header("Authorization") String token, @Path("chatId") Long chatId, @Body Message message);
    @GET("api/conversation/{chatId}/user")
    Call<List<User>> getConversationUsers(@Header("Authorization") String token, @Path("chatId") Long chatId);
    @DELETE("api/conversation/{chatId}/message/{messageId}")
    Call<ResponseBody> deleteMessage(@Header("Authorization") String token, @Path("chatId") Long chatId, @Path("messageId") Long messageId);
    @POST("api/conversation/{chatId}/leave")
    Call<ResponseBody> leave(@Header("Authorization") String token, @Path("chatId") Long chatId);
}
