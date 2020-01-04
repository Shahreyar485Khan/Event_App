//package com.example.eventapp;
//
//import android.app.Activity;
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import static android.content.ContentValues.TAG;
//
//public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
//
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the InstanceID token
//     * is initially generated so this is where you would retrieve the token.
//     */
////    @Override
////    public void onNewToken(String token) {
////        Log.d(TAG, "Refreshed token: " + token);
////
////        // If you want to send messages to this application instance or
////        // manage this apps subscriptions on the server side, send the
////        // Instance ID token to your app server.
////        sendRegistrationToServer(token);
////    }
////
////    private void sendRegistrationToServer(String token) {
////
////        SharedPreferenceManager.getInstance(getApplicationContext()).saveDeviceToken(token);
////    }
//
//    private static final String TAG="MyFirebaseInstanceServi";
//
//    @Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        String refreshedToken =           FirebaseInstanceId.getInstance().getToken();
//        FirebaseMessaging.getInstance().subscribeToTopic("all");
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        /* If you want to send messages to this application instance or manage this apps subscriptions on the server side, send the Instance ID token to your app server.*/
//
//        sendRegistrationToServer(refreshedToken);
//    }
//
//    private void sendRegistrationToServer(String refreshedToken) {
//        Log.d("TOKEN ", refreshedToken.toString());
//    }
//}
