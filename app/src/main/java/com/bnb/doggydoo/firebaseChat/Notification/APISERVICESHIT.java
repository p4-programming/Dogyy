package com.bnb.doggydoo.firebaseChat.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APISERVICESHIT {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAABqRjweg:APA91bF2ZIM00Kh6hH5Ffhbh4SqIFekex7sE9Lkze7ZeSmH9E-5uMxoHiwAt2zPJZrm0QCFQmRtIDNPOaBmu6E9IxL4cNxKufstMxKucNfKNpo7nw4YS1FyqF-mbZOTA7ov4p26XBW3c"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);


}
