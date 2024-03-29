package com.example.eventapp.broadcast;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.eventapp.Activities.DisplayEventListActivity;
import com.example.eventapp.Fragments.EventFragment;
import com.example.eventapp.MainActivity;
import com.example.eventapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.app.NotificationCompat;

import static com.example.eventapp.Utils.AlarmManagerUtils.ALARM_ID;
import static com.example.eventapp.Utils.AlarmManagerUtils.ALARM_TIME;
import static com.example.eventapp.Utils.AlarmManagerUtils.TITLE_KEY;

public class AlarmBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm received!!", Toast.LENGTH_SHORT).show();

        String event_title = intent.getStringExtra(TITLE_KEY);
        long alarm_id = intent.getLongExtra(ALARM_ID,0);
        long alarm_time = intent.getLongExtra(ALARM_TIME,0);

        long current_time = System.currentTimeMillis();


        Intent it =  new Intent(context, EventFragment.class);
        createNotification(context, it, "new mensage", event_title, "this is a mensage");

        cancelAlarm(context,alarm_id);

      /*  if (alarm_time == current_time){

            Intent it =  new Intent(context, EventFragment.class);
            createNotification(context, it, "new mensage", event_title, "this is a mensage");

            cancelAlarm(context,alarm_id);
        }*/


       /* try {
            String yourDate = "10/02/2020";
            String yourHour = "17:45:23";
            Date d = new Date();
            DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat hour = new SimpleDateFormat("HH:mm:ss");
            if (date.equals(yourDate) && hour.equals(yourHour)){
                Intent it =  new Intent(context, DisplayEventListActivity.class);
                createNotification(context, it, "new mensage", "body!", "this is a mensage");
            }
        }catch (Exception e){
            Log.i("date","error == "+e.getMessage());
        }*/

    }


    public void createNotification(Context context, Intent intent, CharSequence ticker, CharSequence title, CharSequence descricao){
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(descricao);
        builder.setSmallIcon(R.drawable.home);
        builder.setContentIntent(p);
        Notification n = builder.build();
        //create the notification
        n.vibrate = new long[]{150, 300, 150, 400};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.home, n);
        //create a vibration
        try{

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        }
        catch(Exception e){}
    }

    public void cancelAlarm(Context context,long alarmID){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,(int) alarmID, myIntent, 0);

        alarmManager.cancel(pendingIntent);

    }
}
