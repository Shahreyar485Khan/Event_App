package com.example.eventapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyNotificationManager {

    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;

    private Context mCtx;

    public MyNotificationManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    //the method will show a big notification with an image
    //parameters are title for message title, message for message text, url of the big image and an intent that will open
    //when you will tap on the notification
    public void showBigNotification(String title, String message, String url, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_BIG_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_BIG_NOTIFICATION, notification);
    }

    //the method will show a small notification
    //parameters are title for message title, message for message text and an intent that will open
    //when you will tap on the notification
    public void showSmallNotification(String title, String message, Intent intent) {
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mCtx,
                        ID_SMALL_NOTIFICATION,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mCtx);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(), R.mipmap.ic_launcher))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_SMALL_NOTIFICATION, notification);
    }


//
//    private void createNotificationGroups() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            List<NotificationChannelGroup> list = new ArrayList<>();
//            list.add(new NotificationChannelGroup("groupone", "groupone"));
////            list.add(new NotificationChannelGroup(radioButtonSecond.getText().toString(), radioButtonSecond.getText()));
//
//            NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            notificationManager.createNotificationChannelGroups(list);
//
//        }
//    }
//

    private void sendOnChannel1(String title, String message, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

       Notification notification = new NotificationCompat.Builder(mCtx, "1")
               .setSmallIcon(R.mipmap.ic_launcher)
               .setContentTitle(title)
               .setContentText(message)
               .setPriority(NotificationCompat.PRIORITY_HIGH)
               .setCategory(NotificationCompat.CATEGORY_MESSAGE)
               .build();

       notificationManager.notify(1, notification);
    }

    private void sendOnChannel2(String title, String message, String url, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(url));

       Notification notification = new NotificationCompat.Builder(mCtx, "1")
               .setSmallIcon(R.mipmap.ic_launcher)
               .setContentTitle(title)
               .setContentText(message)
               .setStyle(bigPictureStyle)
               .setPriority(NotificationCompat.PRIORITY_HIGH)
               .setCategory(NotificationCompat.CATEGORY_MESSAGE)
               .build();

       notificationManager.notify(1, notification);
    }

    public void createNotificationChannels(String title, String message, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "1",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationManager manager = mCtx.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

            sendOnChannel1(title, message, intent);

        }
    }

    public void createNotificationChannels(String title, String message, String url, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "1",
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");

            NotificationManager manager = mCtx.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

            sendOnChannel2(title, message, url, intent);

        }
    }


    //The method will return Bitmap from an image URL
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
