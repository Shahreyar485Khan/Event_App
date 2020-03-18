package com.example.eventapp.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Activities.DisplayEventListActivity;
import com.example.eventapp.Activities.DisplayJoinedEventListActivity;
import com.example.eventapp.Activities.TestFragmentHolder;
import com.example.eventapp.Interfaces.DatePickerInterface;
import com.example.eventapp.R;
import com.example.eventapp.Interfaces.TimePickerInterface;
import com.example.eventapp.Utils.AlarmManagerUtils;
import com.example.eventapp.Utils.DateUtils;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.example.eventapp.Utils.TextUtils;
import com.example.eventapp.broadcast.AlarmBroadcast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EventFragment extends Fragment implements View.OnClickListener, TimePickerInterface, DatePickerInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    PhpMethodsUtils phpMethodsUtils;
    AlarmManagerUtils alarmManagerUtils;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView startTimeTv, startDateTv, endTimeTv, endDateTv;
    private LinearLayout createEvent,eventList,joinedEventList;
    private EditText title, location, discription;
    private int st_month,st_dayOfMonth,st_year,st_hourOfDay,st_minutes;
    private int end_month,end_dayOfMonth,end_year,end_hourOfDay,end_minutes;
    public static final String START = "start";
    public static final String END = "end";
    static int REQ = 1;
    Random random;
    InterstitialAd interstitialAd;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public EventFragment() {
        // Required empty public constructor
    }


    DateUtils dateUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        initUi();
        registerListeners();

        random = new Random();

        dateUtils = new DateUtils();
        phpMethodsUtils = new PhpMethodsUtils(getActivity());
        alarmManagerUtils = new AlarmManagerUtils(getActivity());

        /*String dateInMilli = getArguments().getString("dateinmilli");
        String date = getArguments().getString("date");
        Toast.makeText(getActivity(), "Date: " + dateInMilli, Toast.LENGTH_SHORT).show();

        String fullDate = dateUtils.milliToDate(Long.parseLong(dateInMilli));

        Toast.makeText(getActivity(), "" + fullDate, Toast.LENGTH_SHORT).show();*/

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = formatter.format(todayDate);

        SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
        String  currentTime = df.format(todayDate.getTime());

        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
        String  currentTime12 = df.format(todayDate.getTime());

        Calendar cal = Calendar.getInstance();
        cal.setTime(todayDate);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);

        startDateTv.setText(currentDate);
        endDateTv.setText(currentDate);
        startTimeTv.setText(currentTime12);
        endTimeTv.setText(currentTime12);


        st_dayOfMonth = dayOfMonth;
        st_month = month;
        st_year = year;
        st_hourOfDay = hours;
        st_minutes = minutes;
        end_dayOfMonth = dayOfMonth;
        end_month = month;
        end_year = year;
        end_hourOfDay = hours;
        end_minutes = minutes;




    }

    private void registerListeners() {

        startTimeTv.setOnClickListener(this);
        startDateTv.setOnClickListener(this);

        endTimeTv.setOnClickListener(this);
        endDateTv.setOnClickListener(this);

        createEvent.setOnClickListener(this);
        eventList.setOnClickListener(this);
        joinedEventList.setOnClickListener(this);
    }

    void initUi() {


        startTimeTv = getActivity().findViewById(R.id.event_time_tv);
        startDateTv = getActivity().findViewById(R.id.event_date_tv);
        endTimeTv = getActivity().findViewById(R.id.event_end_time_tv);
        endDateTv = getActivity().findViewById(R.id.event_end_date_tv);
        createEvent = getActivity().findViewById(R.id.btn_create_event);
        eventList = getActivity().findViewById(R.id.btn_event_list);
        joinedEventList = getActivity().findViewById(R.id.btn_joined_event);

        title = getActivity().findViewById(R.id.title_et);
        location = getActivity().findViewById(R.id.location_et);
        discription = getActivity().findViewById(R.id.discription_et);


        endTimeTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        endDateTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        startDateTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        startTimeTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        reqNewInterstitial();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.event_time_tv:{

                TimePickerFragment newFragment = new TimePickerFragment();
                newFragment.time(START,this, startTimeTv);
                newFragment.show(getActivity().getFragmentManager(), "TimePicker");

                break;
            }
            case R.id.event_date_tv:{

                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.date(START,this, startDateTv);
                dateFragment.show(getActivity().getFragmentManager(), "DatePicker");

                break;
            }
            case R.id.event_end_time_tv:{

                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.time(END,this, endTimeTv);
                timePickerFragment.show(getActivity().getFragmentManager(), "TimePicker");

                break;
            }
            case R.id.event_end_date_tv:{

                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.date(END,this, endDateTv);
                dateFragment.show(getActivity().getFragmentManager(), "DatePicker");

                break;
            }
            case R.id.btn_create_event:{

            //    Toast.makeText(getActivity(), "listen", Toast.LENGTH_SHORT).show();


                String title_ = title.getText().toString();
                String location_ = location.getText().toString();
                String disc = discription.getText().toString();
                String start_date = startDateTv.getText().toString();
                String end_date = endDateTv.getText().toString();
                String start_time = startTimeTv.getText().toString();
                String end_time = endTimeTv.getText().toString();

                if (TextUtils.isNotEmpty(title)) {

                    // setEventReminder(start_time,end_time,start_date,end_date);
                    Log.d("event_reminder", "START    " + st_hourOfDay + " " + st_minutes + ": " + st_month + st_dayOfMonth + st_year);
                    Log.d("event_reminder", "END     " + end_hourOfDay + " " + end_minutes + ": " + end_month + end_dayOfMonth + end_year);

                    Calendar current = Calendar.getInstance();

                    Calendar cal_event_start = Calendar.getInstance();
                    cal_event_start.set(st_year,
                            st_month,
                            st_dayOfMonth,
                            st_hourOfDay,
                            st_minutes,
                            00);

                    Calendar cal_event_end = Calendar.getInstance();
                    cal_event_end.set(end_year,
                            end_month,
                            end_dayOfMonth,
                            end_hourOfDay,
                            end_minutes,
                            00);

                    if (cal_event_start.compareTo(current) <= 0 && cal_event_end.compareTo(current) <= 0) {
                        //The set Date/Time already passed
                        Toast.makeText(getActivity(), "Invalid Date/Time", Toast.LENGTH_LONG).show();
                    } else {
                        phpMethodsUtils.getCurrentDevice();

                        phpMethodsUtils.addEvent(PhpMethodsUtils.currentDeviceId,title_, location_, disc, start_time, start_date, end_date, end_time);
                        alarmManagerUtils.setEventReminder(title_,cal_event_start);
                        alarmManagerUtils.setEventReminder(title_,cal_event_end);
                    }
                }else{
                    title.setError("This field cannot be empty");
                    Toast.makeText(getActivity(), "Please give event title", Toast.LENGTH_LONG).show();
                }

                break;
            }
            case R.id.btn_event_list:{

                //Toast.makeText(getActivity(), "listen", Toast.LENGTH_SHORT).show();


                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    reqNewInterstitial();
                    startActivity(new Intent(getActivity(), DisplayEventListActivity.class));

                }
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        reqNewInterstitial();
                        startActivity(new Intent(getActivity(), DisplayEventListActivity.class));

                    }
                });



                break;
            }

            case R.id.btn_joined_event:{

               // Toast.makeText(getActivity(), "listen", Toast.LENGTH_SHORT).show();

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    reqNewInterstitial();
                    startActivity(new Intent(getActivity(), DisplayJoinedEventListActivity.class));

                }
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        reqNewInterstitial();
                        startActivity(new Intent(getActivity(), DisplayJoinedEventListActivity.class));

                    }
                });


                break;
            }

        }

    }


    public void reqNewInterstitial() {
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @Override
    public void onSetTimeBtnClick(String status,int hourOfDay, int minutes,String time, TextView textView) {
//        TextView tv = (TextView) getActivity().findViewById(R.id.event_time_tv);



        Calendar current = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(st_year,st_month,st_dayOfMonth,hourOfDay,minutes,00);

        if (cal.compareTo(current) <= 0) {
            //The set Date/Time already passed
            Toast.makeText(getActivity(),
                    "Invalid Date/Time",
                    Toast.LENGTH_LONG).show();
        } else {

            textView.setText(time);

            switch (status){
                case START:{
                    this.st_hourOfDay = hourOfDay;
                    this.st_minutes = minutes;
                    break;
                }
                case END:{
                    this.end_hourOfDay = hourOfDay;
                    this.end_minutes = minutes;
                    break;
                }
            }
        }
    }

    @Override
    public void onSetDate(String status, int month,int dayOfMonth, int year,String date, TextView textview) {


        Calendar current = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(year,month-1,dayOfMonth);

        if (cal.compareTo(current) <= 0) {
            //The set Date/Time already passed
            Toast.makeText(getActivity(),
                    "Invalid Date/Time",
                    Toast.LENGTH_LONG).show();
        } else {

            textview.setText(date);

            switch (status){

                case START:{
                    this.st_month = month;
                    this.st_dayOfMonth = dayOfMonth;
                    this.st_year = year;
                    break;
                }
                case END:{
                    this.end_month = month;
                    this.end_dayOfMonth = dayOfMonth;
                    this.end_year = year;
                    break;
                }
            }
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
