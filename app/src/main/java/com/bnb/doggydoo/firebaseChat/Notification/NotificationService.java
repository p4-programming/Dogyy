package com.bnb.doggydoo.firebaseChat.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.bnb.doggydoo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import static android.content.Context.MODE_PRIVATE;

public class NotificationService extends BroadcastReceiver {
    public static final String CHANNELID = "doggydoo";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager noti = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput!=null) {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            String userid = firebaseAuth.getCurrentUser().getUid();


            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


            SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS", MODE_PRIVATE);
            String friendid = sharedPreferences.getString("friendid", "");


            SimpleDateFormat formatter= new SimpleDateFormat( "HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String currenttime = formatter.format(date);



            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sender", userid);
            hashMap.put("receiver", friendid);
            hashMap.put("time", currenttime);

            firebaseFirestore.collection("Messages").document(currenttime).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });


            SharedPreferences shf = context.getSharedPreferences("NEWPREFS", MODE_PRIVATE);
            int shit = shf.getInt("values", 0);

            Notification repliedNotification =
                    new NotificationCompat.Builder(context, CHANNELID)
                            .setSmallIcon(
                                    R.drawable.ic_notification_icon)
                            .setContentText("Reply received")
                            .build();

            noti.notify(shit,
                    repliedNotification);







        }
    }
}
