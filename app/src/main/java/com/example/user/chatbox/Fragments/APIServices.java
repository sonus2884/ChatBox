package com.example.user.chatbox.Fragments;

import com.example.user.chatbox.Notification.MyResponse;
import com.example.user.chatbox.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServices {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAmqtehME:APA91bHVXDIuSurNPtzj4PlvxONLZBH_gzke4mocLjfbWSpn9Xf0Msg1UMAwDRf0dXbj3yA1qdFXWu-BXXZbfnAfCux5bs5zrC9fVpjK20Bthi3BThKWyYcatmR20w7eIMI0u-zGOJ5p"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
