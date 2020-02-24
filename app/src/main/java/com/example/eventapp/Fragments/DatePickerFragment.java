package com.example.eventapp.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.eventapp.Interfaces.DatePickerInterface;
import com.example.eventapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerInterface onSetDateListener;
    TextView textView;
    String status;

    public void date(String status,DatePickerInterface onSetDateListener, TextView textView)
    {
        this.onSetDateListener = onSetDateListener;
        this.textView = textView;
        this.status = status;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        //Create and return a new instance of TimePickerDialog
        /*
            public constructor.....
            TimePickerDialog(Context context, int theme,
             TimePickerDialog.OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView)

            The 'theme' parameter allow us to specify the theme of TimePickerDialog

            .......List of Themes.......
            THEME_DEVICE_DEFAULT_DARK
            THEME_DEVICE_DEFAULT_LIGHT
            THEME_HOLO_DARK
            THEME_HOLO_LIGHT
            THEME_TRADITIONAL

         */

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                this, year, month, dayOfMonth);


        //You can set a simple text title for TimePickerDialog
        //tpd.setTitle("Title Of Time Picker Dialog");

        /*.........Set a custom title for picker........*/
        TextView tvTitle = new TextView(getActivity());
        tvTitle.setText("DatePickerDialog Title");
        tvTitle.setBackgroundColor(Color.parseColor("#EEE8AA"));
        tvTitle.setPadding(5, 3, 5, 3);
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        datePickerDialog.setCustomTitle(tvTitle);
        /*.........End custom title section........*/

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        TextView tv = (TextView) getActivity().findViewById(R.id.event_date_tv);

        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(year, month+1, dayOfMonth-1);
        String dayOfWeek = simpledateformat.format(date);

        String dateStr = month+1+"/"+dayOfMonth+"/"+year;
/*
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        //Calendar cal = Calendar.getInstance();

        try {
            Date datee = formatter.parse(dateStr);

         //   cal.setTime(date);
            Log.d("date",datee.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }*/



      //  date.toString();




//        tv.setText(dayOfWeek+", "+month+"/"+dayOfMonth+"/"+year);

        onSetDateListener.onSetDate(status,month+1,dayOfMonth,year,dateStr, textView);
    }
}