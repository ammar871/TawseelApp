package com.ammar.tawseel.notifecation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.ammar.tawseel.R;
import com.ammar.tawseel.ui.ConfirmActivity;
import com.ammar.tawseel.ui.home.HomeActivity;
import com.facebook.common.Common;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Objects;
import java.util.Random;


public class NotificationClass {
    RemoteMessage notification;
    Context context;
    NotificationClass notificationClass;

    public NotificationClass(RemoteMessage remoteMessage, Context applicationContext) {
        this.notification = remoteMessage;
        this.context = applicationContext;
        notificationClass = new NotificationClass();
    }

    public NotificationClass() {
    }

    private void sendOreoNotification(RemoteMessage order) {
        String body = null;
        String title = null;
        String id;

        if (order.getData().get("msg_from") != null) {
            if (order.getData().get("type").equals("text")){
                body = order.getData().get("message");

            }else if (order.getData().get("type").equals("image")){
                body =context.getString(R.string.send_image);

            }else if (order.getData().get("type").equals("video")){
                body =context.getString(R.string.send_video);

            }else if (order.getData().get("type").equals("map")){
                body =context.getString(R.string.send_location);

            }else if (order.getData().get("type").equals("audio")){
                body =context.getString(R.string.audio);

            }


            title = order.getData().get("new Message");
            id = order.getData().get("order_id");
        } else {


            body = getMessageBody(Objects.requireNonNull(order.getData().get("type")),order.getData().get("target"));
            title = order.getData().get("new Notification");
            id = order.getData().get("id");
        }

//        Paper.book().write("id",Integer.parseInt(id));
//        Paper.book().write("flag",true);


        int j = (int) System.currentTimeMillis();

        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


//        intent.putExtra("id", Integer.parseInt(id));
//        intent.putExtra("flag", true);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(context);
        NotificationCompat.Builder builder = oreoNotification.builder(title, body, pendingIntent, defaultSound, R.drawable.image_tasel);


        final int min = 10;
        final int max = 800;
        final int random = new Random().nextInt((max - min) + 1) + min;

        oreoNotification.getNotificationManager().notify(random, builder.build());
    }

//    private String getMessageBody(int status) {
//        switch (status) {
//            case 0:
//                return "تم التحويل الى ارسال";
//            case 1:
//                return "جاري المعالجة";
//            case 3:
//                return "النجاح ف المهمة ";
//            default:
//                return "legh ";
//        }
//    }

    private String getMessageBody(String status, String target) {
        switch (status) {
            case "deliver-cancel":
                return context.getString(R.string.new_bill) + " " + target;
            case "deliver-order":
                return context.getString(R.string.noty_delviry_order) + " " + target;
            case "deliver-confirm":
                return context.getString(R.string.deliverd_order) + " " + target;
            case "accept-order":
                return context.getString(R.string.accept_order) + " " + target;
            case "refuse-order":
                return context.getString(R.string.refuse_order) + " " + target;
            case "user-add-rate":
                return context.getString(R.string.rate_dliviry) + " " + target;
            case "paid-bill":
                return context.getString(R.string.paid_bill_) + " " + target;

            case "cancel-bill":
                return context.getString(R.string.cancel_bill) + " " + target;
            case "admin-accept-order":
            case "admin-refues-order":
            case "admin-id-papers":
            case "admin-activate-account":
            case "admin-financial-boost":
            case "admin-new-rate":
            case "admin-cash":
                return  target;

            default:
                return target;
        }
    }

    private void sendNotification(RemoteMessage order) {

        String body = null;
        String title = null;
        String id;

        if (order.getData().get("msg_from") != null) {
            body = order.getData().get("message");
            title = "new Message";
            id = order.getData().get("order_id");
        } else {
            body = order.getData().get("target");
            title = "new Notification";
            id = order.getData().get("id");
        }

//        Paper.book().write("id",Integer.parseInt(id));
//        Paper.book().write("flag",true);

        int j = (int) System.currentTimeMillis();

        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);


//        intent.putExtra("id", Integer.parseInt(id));
//        intent.putExtra("flag", true);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, j, intent, PendingIntent.FLAG_ONE_SHOT);

//        Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/" + R.raw.pristine);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.ammar.tawseel";

        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.image_tasel)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_MAX);

        final int min = 10;
        final int max = 800;
        final int random = new Random().nextInt((max - min) + 1) + min;

        notificationManager.notify(random, notificationBuilder.build());
    }

    public NotificationClass getNotificationClass() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendOreoNotification(notification);
        } else
            sendNotification(notification);

        return notificationClass;
    }
}
