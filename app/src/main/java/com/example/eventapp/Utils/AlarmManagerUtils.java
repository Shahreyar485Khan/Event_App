package com.example.eventapp.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.eventapp.broadcast.AlarmBroadcast;

import java.util.Calendar;
import java.util.Random;

public class AlarmManagerUtils {

    Context context;
    public static final String TITLE_KEY = "title_key";
    public static final String ALARM_ID = "alarm_id";
    public static final String ALARM_TIME = "alarm_time";
    Random rand = new Random();


    public AlarmManagerUtils(Context context){

        this.context = context;
    }


    public void setEventReminder(String event_title,Calendar calendar){

        int alarm_id = rand.nextInt(2000);
     //   long alarm_id =  System.currentTimeMillis();
        long alarm_time =  calendar.getTimeInMillis();

        Intent intent = new Intent(context, AlarmBroadcast.class);
        intent.putExtra(TITLE_KEY,event_title);
        intent.putExtra(ALARM_ID,alarm_id);
        intent.putExtra(ALARM_TIME,alarm_time);
        //  intent.putExtra()

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm_id, intent,0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        // Start alarm using notification time
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + diffTime, pendingIntent);

        Toast.makeText(context, "Alarm set", Toast.LENGTH_SHORT).show();

    }


}
