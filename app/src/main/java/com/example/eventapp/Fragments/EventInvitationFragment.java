package com.example.eventapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Adapters.InvitationAdapter;
import com.example.eventapp.Adapters.RequestAdapter;
import com.example.eventapp.R;
import com.example.eventapp.Utils.AlarmManagerUtils;
import com.example.eventapp.Utils.DateFormat;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.example.eventapp.Utils.StringFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventInvitationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventInvitationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInvitationFragment extends Fragment implements InvitationAdapter.AdapterListener, InvitationAdapter.InvitationAdapterOnClickHandler{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

  //  private RequestsFragment.OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private InvitationAdapter invitationAdapter;


    private List<String> locationList;
    private List<String> event_idList;
    private List<String> titleList;
    private List<String> descList;
    private List<String> st_timeList;
    private List<String> end_timeList;
    private List<String> st_dateList;
    private List<String> end_dateList;

    private List<String> senderName;
    private List<String> senderEmail;
    private List<String> id;
    private List<String> recipientName;
    //    private List<String> name;
    private ProgressDialog progressDialog;

    PhpMethodsUtils phpMethodsUtils;
    AlarmManagerUtils alarmManagerUtils;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EventInvitationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventInvitationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventInvitationFragment newInstance(String param1, String param2) {
        EventInvitationFragment fragment = new EventInvitationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_invitation, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onStart() {
        super.onStart();


        event_idList = new ArrayList<>();
        titleList = new ArrayList<>();
        locationList = new ArrayList<>();
        descList = new ArrayList<>();
        st_timeList = new ArrayList<>();
        end_timeList = new ArrayList<>();
        st_dateList = new ArrayList<>();
        end_dateList = new ArrayList<>();


        senderName = new ArrayList<>();
        senderEmail = new ArrayList<>();
        id = new ArrayList<>();
        recipientName = new ArrayList<>();

        phpMethodsUtils = new PhpMethodsUtils(getActivity());
        alarmManagerUtils = new AlarmManagerUtils((getActivity()));

        recyclerView = getActivity().findViewById(R.id.invitation_recycler_view);
        invitationAdapter = new InvitationAdapter(this, this, getActivity());



        getPendingInvitationList("pending");
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
    public void btnOnClick(View v, int position) {

        String email = v.getTag(R.string.sender_email).toString();
        String name = v.getTag(R.string.sender_name).toString();
        String id = v.getTag(R.string.sender_id).toString();
        String event_id = v.getTag(R.string.event_id).toString();
        String start_date = v.getTag(R.string.event_st_date).toString();
        String end_date = v.getTag(R.string.event_end_date).toString();
        String start_time = v.getTag(R.string.event_st_time).toString();
        String end_time = v.getTag(R.string.event_end_time).toString();
        String title = v.getTag(R.string.event_title).toString();


        if (v.getId() == R.id.event_btn_join) {
           boolean result =  phpMethodsUtils.acceptEventInvitation(event_id,id, "accepted",email, name);
           if (result){



               Calendar st_reminder = DateFormat.getAlarmCalender(start_time,start_date);
               alarmManagerUtils.setEventReminder(title,st_reminder);
               Calendar end_reminder = DateFormat.getAlarmCalender(end_time,end_date);
               alarmManagerUtils.setEventReminder(title,end_reminder);

           }else{
               Toast.makeText(getActivity(), "Unable to accept invitation", Toast.LENGTH_SHORT).show();
           }

        } else if (v.getId() == R.id.event_btn_reject) {
            boolean result = phpMethodsUtils.acceptEventInvitation(event_id, id, "rejected", email, name);
            if (!result) {
                Toast.makeText(getActivity(), "Unable to reject invitation", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onClick(String bookmarksStr) {

    }




    public void getPendingInvitationList(String reqStatus) {



        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        //Toast.makeText(getActivity(), "out", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_GET_PENDING_INVITATION_LIST+"recipient_id="+PhpMethodsUtils.currentDeviceId+"&req_status="+reqStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("EventInvitationFragment",response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                Toast.makeText(getActivity(), "IN", Toast.LENGTH_SHORT).show();
                                JSONArray jsonDevices = obj.getJSONArray("requests");
                              //  JSONArray jsonEvents = obj.getJSONArray("events");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
//                                    devices.add(d.getString("email"));
                                    String sender_name = d.getString("sender_name");
                                    sender_name = StringFormat.removebrakets(sender_name);
                                    sender_name = StringFormat.removeQoutes(sender_name);

                                    String sender_email = d.getString("sender_email");
                                    sender_email = StringFormat.removebrakets(sender_email);
                                    sender_email = StringFormat.removeQoutes(sender_email);

                                    String event_id = d.getString("event_id");
                                    event_id = StringFormat.removebrakets(event_id);
                                    event_id = StringFormat.removeQoutes(event_id);

                                    String event_title = d.getString("event_title");
                                    event_title = StringFormat.removebrakets(event_title);
                                    event_title = StringFormat.removeQoutes(event_title);

                                    String event_location = d.getString("event_location");
                                    event_location = StringFormat.removebrakets(event_location);
                                    event_location = StringFormat.removeQoutes(event_location);

                                    String event_desc = d.getString("event_desc");
                                    event_desc = StringFormat.removebrakets(event_desc);
                                    event_desc = StringFormat.removeQoutes(event_desc);

                                    String event_st_time = d.getString("event_st_time");
                                    event_st_time = StringFormat.removebrakets(event_st_time);
                                    event_st_time = StringFormat.removeQoutes(event_st_time);

                                    String event_end_time = d.getString("event_end_time");
                                    event_end_time = StringFormat.removebrakets(event_end_time);
                                    event_end_time = StringFormat.removeQoutes(event_end_time);

                                    String event_st_date = d.getString("event_st_date");
                                    event_st_date = StringFormat.removebrakets(event_st_date);
                                    event_st_date = StringFormat.removeQoutes(event_st_date);
                                    event_st_date = StringFormat.removeSlash(event_st_date);


                                    String event_end_date = d.getString("event_end_date");
                                    event_end_date = StringFormat.removebrakets(event_end_date);
                                    event_end_date = StringFormat.removeQoutes(event_end_date);
                                    event_end_date = StringFormat.removeSlash(event_end_date);

                                    Log.d("eventid","event id    "+event_id+"   event title "+event_title);



                                    senderName.add(sender_name);
                                    id.add(d.getString("sender_id"));
                                    senderEmail.add(sender_email);

                                    event_idList.add(event_id);
                                    titleList.add(event_title);
                                    locationList.add(event_location);
                                    descList.add(event_desc);
                                    st_timeList.add(event_st_time);
                                    end_timeList.add(event_end_time);
                                    st_dateList.add(event_st_date);
                                    end_dateList.add(event_end_date);

                                    //recipientName.add(d.getString("recipientname"));
//                                    id.add(d.getString("id"));

                                }


                                Log.d("RequestFragment", "check "+senderName);
                                invitationAdapter.setSenderList(senderName);
                                invitationAdapter.setIdList(id);
                                invitationAdapter.setEmailList(senderEmail);

                                invitationAdapter.setEventIdList(event_idList);
                                invitationAdapter.setTitleList(titleList);
                                invitationAdapter.setLocationList(locationList);
                                invitationAdapter.setDescList(descList);
                                invitationAdapter.setSt_timeList(st_timeList);
                                invitationAdapter.setEnd_timeList(end_timeList);
                                invitationAdapter.setSt_dateList(st_dateList);
                                invitationAdapter.setEnd_dateList(end_dateList);


                                recyclerView.setAdapter(invitationAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "errorr", Toast.LENGTH_SHORT).show();
                    }
                }) {
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("RequestFragment", "currentid "+ PhpMethodsUtils.currentDeviceId);
                params.put("req_status", reqStatus);
                params.put("recipient_id", PhpMethodsUtils.currentDeviceId);

                return params;
            }*/
        };

        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);
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
