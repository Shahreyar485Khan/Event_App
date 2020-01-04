package com.example.eventapp.Services;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.eventapp.MainActivity;
import com.example.eventapp.MyNotificationManager;
import com.example.eventapp.Utils.SharedPreferenceManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

        SharedPreferenceManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            //if there is no image
            if (imageUrl.equals("null")) {
                //displaying small notification

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mNotificationManager.createNotificationChannels(title, message, intent);
                }
                else {
                    mNotificationManager.showSmallNotification(title, message, intent);
                }

            } else {
                //if there is an image
                //displaying a big notification
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mNotificationManager.createNotificationChannels(title, message, imageUrl, intent);
                }
                else {
                    mNotificationManager.showBigNotification(title, message, imageUrl, intent);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}