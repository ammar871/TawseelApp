package com.ammar.tawseel.notifecation.me;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ammar.tawseel.R;
import com.ammar.tawseel.notifecation.NotificationClass;
import com.ammar.tawseel.pojo.data.Message;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Bitmap image;
    private NotificationManager mManager;


    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        String click_action = remoteMessage.getData().get("click_action");
//        final String title = remoteMessage.getData().get("title");
//        String body = remoteMessage.getData().get("body");

        if (remoteMessage.getData().get("msg_from") != null){
            Message message = new Message();
            message.setFrom(remoteMessage.getData().get("msg_from"));
            message.setSenderType(remoteMessage.getData().get("msg_to"));
            message.setOrderId(remoteMessage.getData().get("order_id"));
            message.setReciverType(remoteMessage.getData().get("reciver_type"));
            message.setType(remoteMessage.getData().get("type"));
            message.setSenderType(remoteMessage.getData().get("sender_type"));
            message.setMessage(remoteMessage.getData().get("message"));

            Intent intent = new Intent("com.codinginflow.EXAMPLE_ACTION");
            intent.putExtra("com.codinginflow.EXTRA_TEXT", message);
            sendBroadcast(intent);
        }

        new NotificationClass(remoteMessage, getApplicationContext()).getNotificationClass();




    }

    public NotificationManager getManger() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

}
