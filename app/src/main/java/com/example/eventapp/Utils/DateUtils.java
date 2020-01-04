package com.example.eventapp.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public DateUtils(){

    }
    public String getDayName(int dayofWeek) {

        String dayName = null;
        switch (dayofWeek) {
            case 1:
                dayName = "Sunday";
                break;
            case 2:
                dayName = "Monday";
                break;
            case 3:
                dayName = "Tuesday";
                break;
            case 4:
                dayName = "Wednesday";
                break;
            case 5:
                dayName = "Thursday";
                break;
            case 6:
                dayName = "Friday";
                break;
            case 7:
                dayName = "Saturday";
                break;
        }
        return dayName;
    }

    public long dateToMilliseconds(String date)
    {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    public String milliToDate(Long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH)+1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mWeekDay = calendar.get(Calendar.DAY_OF_WEEK);
        String sWeekDay = getDayName(mWeekDay);

        String date = sWeekDay+", "+mMonth+"/"+mDay+"/"+mYear;

        return date;
    }
}
