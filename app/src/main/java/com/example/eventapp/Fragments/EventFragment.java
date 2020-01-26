package com.example.eventapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Activities.DisplayEventListActivity;
import com.example.eventapp.Activities.TestFragmentHolder;
import com.example.eventapp.Interfaces.DatePickerInterface;
import com.example.eventapp.R;
import com.example.eventapp.Interfaces.TimePickerInterface;
import com.example.eventapp.Utils.DateUtils;
import com.example.eventapp.Utils.PhpMethodsUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView startTimeTv, startDateTv, endTimeTv, endDateTv;
    Button createEvent,eventList;
    EditText title, location, discription;

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

        dateUtils = new DateUtils();
        phpMethodsUtils = new PhpMethodsUtils(getActivity());

        /*String dateInMilli = getArguments().getString("dateinmilli");
        String date = getArguments().getString("date");
        Toast.makeText(getActivity(), "Date: " + dateInMilli, Toast.LENGTH_SHORT).show();

        String fullDate = dateUtils.milliToDate(Long.parseLong(dateInMilli));

        Toast.makeText(getActivity(), "" + fullDate, Toast.LENGTH_SHORT).show();*/

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);

        startDateTv.setText(todayString);
        endDateTv.setText(todayString);
        startTimeTv.setText("8 : 00 AM");
        endTimeTv.setText("9 : 00 AM");
    }

    private void registerListeners() {

        startTimeTv.setOnClickListener(this);
        startDateTv.setOnClickListener(this);

        endTimeTv.setOnClickListener(this);
        endDateTv.setOnClickListener(this);

        createEvent.setOnClickListener(this);
        eventList.setOnClickListener(this);
    }

    void initUi() {
        startTimeTv = getActivity().findViewById(R.id.event_time_tv);
        startDateTv = getActivity().findViewById(R.id.event_date_tv);
        endTimeTv = getActivity().findViewById(R.id.event_end_time_tv);
        endDateTv = getActivity().findViewById(R.id.event_end_date_tv);
        createEvent = getActivity().findViewById(R.id.btn_create_event);
        eventList = getActivity().findViewById(R.id.btn_event_list);

        title = getActivity().findViewById(R.id.title_et);
        location = getActivity().findViewById(R.id.location_et);
        discription = getActivity().findViewById(R.id.discription_et);
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
                newFragment.time(this, startTimeTv);
                newFragment.show(getActivity().getFragmentManager(), "TimePicker");

                break;
            }
            case R.id.event_date_tv:{

                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.date(this, startDateTv);
                dateFragment.show(getActivity().getFragmentManager(), "DatePicker");

                break;
            }
            case R.id.event_end_time_tv:{

                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.time(this, endTimeTv);
                timePickerFragment.show(getActivity().getFragmentManager(), "TimePicker");

                break;
            }
            case R.id.event_end_date_tv:{

                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.date(this, endDateTv);
                dateFragment.show(getActivity().getFragmentManager(), "DatePicker");

                break;
            }
            case R.id.btn_create_event:{

                Toast.makeText(getActivity(), "listen", Toast.LENGTH_SHORT).show();
                phpMethodsUtils.addEvent(title.getText().toString(),location.getText().toString(),discription.getText().toString(),startTimeTv.getText().toString(),startDateTv.getText().toString(),endDateTv.getText().toString(),endTimeTv.getText().toString() );


                break;
            }
            case R.id.btn_event_list:{

                Toast.makeText(getActivity(), "listen", Toast.LENGTH_SHORT).show();

              // TestFragmentHolder testFragmentHolder = new TestFragmentHolder();
              //  testFragmentHolder.replaceFragment(v);

                startActivity(new Intent(getActivity(), DisplayEventListActivity.class));



               /*  SearchFragment  nextFrag= new SearchFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.viewpager, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();*/


                break;
            }

        }

    }

    @Override
    public void onSetTimeBtnClick(String time, TextView textView) {
//        TextView tv = (TextView) getActivity().findViewById(R.id.event_time_tv);

        textView.setText(time);
    }

    @Override
    public void onSetDate(String date, TextView textview) {
        textview.setText(date);
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
