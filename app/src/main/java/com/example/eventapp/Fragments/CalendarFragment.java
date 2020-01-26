package com.example.eventapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.Activities.ParentActivity;
import com.example.eventapp.R;
import com.example.eventapp.Utils.DateUtils;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "MainActivity";

    private CompactCalendarView compactCalendarView;

    private ActionBar toolbar;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy/MM/dd");
    private DateUtils dateUtils;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        dateUtils = new DateUtils();

        compactCalendarView = (CompactCalendarView) getActivity().findViewById(R.id.compactcalendar_view);

        // below allows you to configure color for the current day in the month
        // compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.black));
        // below allows you to configure colors for the current day the user has selected
        // compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.dark_red));
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);
        //compactCalendarView.setIsRtl(true);

        compactCalendarView.invalidate();

        //set initial title
        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

//                Toast.makeText(getActivity(), ""+dateClicked, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "DateClicked"+milliseconds(dateClicked.toString()), Toast.LENGTH_SHORT).show();

                Long dateInMilli = dateUtils.dateToMilliseconds(dateFormatForDisplaying.format(dateClicked));

                Bundle dateBundle = new Bundle();
                dateBundle.putString("dateinmilli", dateInMilli.toString());
                dateBundle.putString("date", dateClicked.toString());

               /* RequestsFragment nextFrag= new RequestsFragment();
                nextFrag.setArguments(dateBundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();*/



                EventFragment  nextFrag= new EventFragment();
                nextFrag.setArguments(dateBundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();





//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                EventFragment fragmentDemo = EventFragment.newInstance("", "my title");
//                ft.replace(R.id.fragment, fragmentDemo);
//                ft.commit();

//                Toast.makeText(getActivity(), ""+dateFormatForDisplaying.format(dateClicked), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "DateClicked", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "DateClicked"+date, Toast.LENGTH_SHORT).show();


//                Event ev1 = new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");


                Long date = dateUtils.dateToMilliseconds(dateFormatForDisplaying.format(dateClicked));

//                Event ev1 = new Event(Color.GREEN, date, "Some extra data that I want to store.");
//                compactCalendarView.addEvent(ev1);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);
        return view;
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

        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
//        EventFragment nextFrag= new EventFragment();
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment, nextFrag, "findThisFragment")
//                .addToBackStack(null)
//                .commit();
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
