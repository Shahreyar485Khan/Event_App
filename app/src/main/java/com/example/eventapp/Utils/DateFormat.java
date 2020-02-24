package com.example.eventapp.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormat {

    public static final String DATE_FORMAT_2 = "dd/MM/yyyy";


    public static Calendar getAlarmCalender(String time , String date){

        Calendar cal_time = null;
        Calendar cal_date = null;
        Calendar cal_event = null;
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);

        try {
            cal_time = Calendar.getInstance();
            Date date_ = parseFormat.parse(time);
            String _start_time = timeFormatter.format(date_);
            cal_time.setTime(timeFormatter.parse(_start_time));
            int hours = cal_time.get(Calendar.HOUR);
            int minutes = cal_time.get(Calendar.MINUTE);

            cal_date = Calendar.getInstance();
            cal_date.setTime(dateFormatter.parse(date));
            int year = cal_date.get(Calendar.YEAR);
            int month = (cal_date.get(Calendar.MONTH));
            int day = (cal_date.get(Calendar.DAY_OF_MONTH));

            cal_event = Calendar.getInstance();
            cal_event.set(year,month,day,hours,minutes,00);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return cal_event;
    }
    public static Calendar getTime24Format(String time){

        Calendar cal_time = null;
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        try {
            cal_time = Calendar.getInstance();
            Date date_ = parseFormat.parse(time);
            String _start_time = timeFormatter.format(date_);
            cal_time.setTime(timeFormatter.parse(_start_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal_time;
    }
    public static Calendar getDateInCalender(String date){

        Calendar cal = null;

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        try {
            cal = Calendar.getInstance();

            cal.setTime(dateFormatter.parse(date));
            String year = String.valueOf(cal.get(Calendar.YEAR));
            String month = String.valueOf(cal.get(Calendar.MONTH));
            String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
            System.out.println("year: " + year);
            System.out.println("month: " + month+1);
            System.out.println("day: " + day);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }



    public static Date getNearestDate(List<Date> dates, Date targetDate) {
        Date nearestDate = null;
        int index = 0;
        long prevDiff = -1;
        long targetTS = targetDate.getTime();
        for (int i = 0; i < dates.size(); i++) {
            Date date = dates.get(i);
            long currDiff = Math.abs(date.getTime() - targetTS);
            if (prevDiff == -1 || currDiff < prevDiff) {
                prevDiff = currDiff;
                nearestDate = date;
                index = i;
            }
        }
        System.out.println("Nearest Date: " + nearestDate);
        System.out.println("Index: " + index);

        return nearestDate;
    }


    public static Date getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_2);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return today;
    }



}
