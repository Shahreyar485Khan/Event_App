package com.example.eventapp.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Adapters.AllEventListAdapter;
import com.example.eventapp.Adapters.FriendListAdapter;

import com.example.eventapp.Adapters.JoinedMembersListAdapter;
import com.example.eventapp.Fragments.TimePickerFragment;
import com.example.eventapp.Interfaces.DatePickerInterface;
import com.example.eventapp.Interfaces.TimePickerInterface;
import com.example.eventapp.R;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.example.eventapp.Utils.StringFormat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eventapp.Utils.PhpMethodsUtils.currentDeviceId;

public class DisplayEventListActivity extends AppCompatActivity implements FriendListAdapter.AdapterListener, FriendListAdapter.FriendListAdapterOnClickHandler,
                                                                        AllEventListAdapter.ItemClickListener,AllEventListAdapter.AdapterListener,JoinedMembersListAdapter.AdapterListener,JoinedMembersListAdapter.JoinedMembersListAdapterOnClickHandler
                                                                            , TimePickerInterface, DatePickerInterface {
    AllEventListAdapter adapter;
    Button btnSendInvites;
    ImageView imgBack;

    private ArrayList<String> titleList;
    private List<String> locationList;
    private List<String> event_idList;
    private List<String> descList;
    private List<String> st_timeList;
    private List<String> end_timeList;
    private List<String> st_dateList;
    private List<String> end_dateList;

    private ArrayList<String> friendList;
    private ArrayList<String> friend_idList;
    private ArrayList<String> friend_emailList;

    private ArrayList<String> memberList;
    private ArrayList<String> member_idList;
    private ArrayList<String> member_emailList;

    private RecyclerView mRecyclerViewEvent;
    private RecyclerView mRecyclerViewFriend;
    private AllEventListAdapter allEventListAdapter;
    private FriendListAdapter friendListAdapter;
    private JoinedMembersListAdapter joinedMembersListAdapter;
    private ProgressDialog progressDialog;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView txtEmpty;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_list);


        event_idList = new ArrayList<>();
        titleList = new ArrayList<>();
        locationList = new ArrayList<>();
        descList = new ArrayList<>();
        st_timeList = new ArrayList<>();
        end_timeList = new ArrayList<>();
        st_dateList = new ArrayList<>();
        end_dateList = new ArrayList<>();
        txtEmpty = findViewById(R.id.empty_text);

       // imgBack = findViewById(R.id.back);
     /*   imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayEventListActivity.super.onBackPressed();
            }
        });*/


        friendList = new ArrayList<>();
        friend_idList = new ArrayList<>();
        friend_emailList = new ArrayList<>();

        memberList = new ArrayList<>();
        member_idList = new ArrayList<>();
        member_emailList = new ArrayList<>();

        mRecyclerViewEvent = findViewById(R.id.recycler_view_events);
        mRecyclerViewEvent.setVisibility(View.VISIBLE);
        mRecyclerViewFriend = findViewById(R.id.recyclerview_bottom);

        mRecyclerViewEvent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewFriend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        reqNewInterstitial();
        loadAllEvents();

        allEventListAdapter = new AllEventListAdapter(this, titleList,this);




       friendListAdapter = new FriendListAdapter(this, this,this);
       joinedMembersListAdapter = new JoinedMembersListAdapter(this, this,this);




        boolean alarm = (PendingIntent.getBroadcast(this, 0, new Intent("ALARM"), PendingIntent.FLAG_NO_CREATE) == null);

        if(alarm){
            Intent itAlarm = new Intent("ALARM");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,itAlarm,0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 3);
            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),60000, pendingIntent);
        }






    }

    private void loadAllEvents() {

        //Toast.makeText(this, "currrent id"+currentDeviceId, Toast.LENGTH_SHORT).show();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("get Devices...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_GET_TEST_ALL_EVENT+currentDeviceId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.d("DisplayEventList" , "get_all_events "+response);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("events");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
//                                    devices.add(d.getString("email"));
                                    event_idList.add(d.getString("id"));
                                    titleList.add(d.getString("title"));
                                    locationList.add(d.getString("location"));
                                    descList.add(d.getString("description"));
                                    st_timeList.add(d.getString("start_time"));
                                    end_timeList.add(d.getString("end_time"));
                                    st_dateList.add(d.getString("start_date"));
                                    end_dateList.add(d.getString("end_date"));
                                    // devices.add(d.getString("email"));
                                    // id.add(d.getString("id"));

                                }


                                allEventListAdapter.setEventIdList(event_idList);
                                allEventListAdapter.setTitleList(titleList);
                                allEventListAdapter.setLocationList(locationList);
                                allEventListAdapter.setDescList(descList);
                                allEventListAdapter.setSt_timeList(st_timeList);
                                allEventListAdapter.setEnd_timeList(end_timeList);
                                allEventListAdapter.setSt_dateList(st_dateList);
                                allEventListAdapter.setEnd_dateList(end_dateList);

                                mRecyclerViewEvent.setAdapter(allEventListAdapter);


                                if (allEventListAdapter.getItemCount() == 0){

                                    mRecyclerViewEvent.setVisibility(View.GONE);
                                    txtEmpty.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DisplayEventListActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {

           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender1", "4");
                return params;
            }*/

        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void loadAcceptedRequestDevices() {


        if (friendList != null){
            friendList.clear();
        }
        if (friend_idList != null){
            friend_idList.clear();
        }
        if (friend_emailList != null){
            friend_emailList.clear();
        }


      String status =  "accepted";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_GET_PENDING_REQUEST_LIST_GET+"req_status="+status+"&recipient_id="+currentDeviceId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // progressDialog.dismiss();
                        Log.d("FriendList",response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("requests");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);

                                    String sender_fname = d.getString("sender_fname");
                                    sender_fname = StringFormat.removebrakets(sender_fname);
                                    sender_fname = StringFormat.removeQoutes(sender_fname);

                                    String sender_lname = d.getString("sender_lname");
                                    sender_lname = StringFormat.removebrakets(sender_lname);
                                    sender_lname = StringFormat.removeQoutes(sender_lname);

                                    String sender_id = d.getString("sender_id");
                                    sender_id = StringFormat.removebrakets(sender_id);
                                    sender_id = StringFormat.removeQoutes(sender_id);


                                    String sender_email = d.getString("sender_email");
                                    sender_email = StringFormat.removebrakets(sender_email);
                                    sender_email = StringFormat.removeQoutes(sender_email);

                                    friendList.add(sender_fname+" "+sender_lname);
                                    friend_idList.add(sender_id);
                                    friend_emailList.add(sender_email);
                                }


                                friendListAdapter.setNamesList(friendList);
                                friendListAdapter.setEmailList(friend_emailList);
                                friendListAdapter.setIdList(friend_idList);
                                mRecyclerViewFriend.setAdapter(friendListAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DisplayEventListActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {


        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void loadEventMembers(String event_id,String sender_id,String reqStatus) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Push");
        progressDialog.show();


        if (memberList != null){
            memberList.clear();
        }
        if (member_idList != null){
            member_idList.clear();
        }
        if (member_emailList != null){
            member_emailList.clear();
        }


        //Toast.makeText(getActivity(), "out", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_GET_JOINED_MEMBERS_LIST+"sender_id="+PhpMethodsUtils.currentDeviceId+"&req_status="+reqStatus+"&event_id="+event_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("JoinedEventMembers",response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("members");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);

                                    String sender_fname = d.getString("recipient_fname");
                                    sender_fname = StringFormat.removebrakets(sender_fname);
                                    sender_fname = StringFormat.removeQoutes(sender_fname);

                                    String sender_lname = d.getString("recipient_lname");
                                    sender_lname = StringFormat.removebrakets(sender_lname);
                                    sender_lname = StringFormat.removeQoutes(sender_lname);

                                    String sender_id = d.getString("recipient_id");
                                    sender_id = StringFormat.removebrakets(sender_id);
                                    sender_id = StringFormat.removeQoutes(sender_id);


                                    String sender_email = d.getString("recipient_email");
                                    sender_email = StringFormat.removebrakets(sender_email);
                                    sender_email = StringFormat.removeQoutes(sender_email);

                                    memberList.add(sender_fname+" "+sender_lname);
                                    member_idList.add(sender_id);
                                    member_emailList.add(sender_email);
                                }


                                joinedMembersListAdapter.setNamesList(memberList);
                                joinedMembersListAdapter.setEmailList(member_emailList);
                                joinedMembersListAdapter.setIdList(member_idList);
                                mRecyclerViewFriend.setAdapter(joinedMembersListAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DisplayEventListActivity.this, "errorr", Toast.LENGTH_SHORT).show();
                    }
                }) {

        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void sendEventInvitation(String eventId,String senderId, String recipientId ,String recipientEmail) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_SEND_EVENT_INVITATION+"event_id="+eventId+"&sender_id="+senderId+"&recipient_id="+recipientId+"&recipient_email="+recipientEmail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // progressDialog.dismiss();
                        progressDialog.dismiss();
                        Log.d("EventInvitation",response);

                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response);
                            String str = obj.getString("message");
                            Toast.makeText(DisplayEventListActivity.this, "Response     " + str, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                      //  JSONObject obj = null;

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DisplayEventListActivity.this, "Event invites"+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {


        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void deleteEvent(String eventId) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Push");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_DELETE_EVENT+"event_id="+eventId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // progressDialog.dismiss();
                        progressDialog.dismiss();
                        Log.d("EventInvitation",response);

                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response);
                            String str = obj.getString("message");
                            Toast.makeText(DisplayEventListActivity.this, "Response     " + str, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                      //  JSONObject obj = null;

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(DisplayEventListActivity.this, "Event invites"+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {


        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {

        if (mBottomSheetBehavior != null){
            if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                super.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }

    }

    public void reqNewInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @Override
    public void onClick(String bookmarksStr) {

    }


    private void sendEventInvites(View v){

        loadAcceptedRequestDevices();
        FrameLayout parentThatHasBottomSheetBehavior = (FrameLayout) mRecyclerViewFriend.getParent().getParent();
        mBottomSheetBehavior = BottomSheetBehavior.from(parentThatHasBottomSheetBehavior);
        if (mBottomSheetBehavior != null) {
            // setStateText(mBottomSheetBehavior.getState());
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {


                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    //  setOffsetText(slideOffset);
                }
            });
        }


        eventId = v.getTag(R.string.id).toString();

        View peakView = findViewById(R.id.drag_me);
        mBottomSheetBehavior.setPeekHeight(peakView.getHeight());
        // int state = mBottomSheetBehavior.getState();
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        peakView.requestLayout();


    }



    String eventId = null;
    @Override
    public void btnOnClick(View v, int position) {

      //  Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();



        switch (v.getId()){

            case R.id.event_btn_send:{

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    reqNewInterstitial();
                    sendEventInvites(v);

                }
                interstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        reqNewInterstitial();
                        sendEventInvites(v);
                    }
                });

                break;
            }
            case R.id.send_btn_friend:{
                String recipientId = v.getTag(R.string.id).toString();
                String recipientEmail = v.getTag(R.string.email).toString();

                sendEventInvitation(eventId,currentDeviceId,recipientId,recipientEmail);
                Log.d("sending_invites","current_user_id    "+"event_id "+eventId+ currentDeviceId+"Recipient_id    "+recipientId+"recipient_email  "+recipientEmail);

                break;
            }
            case R.id.event_btn_all_join:{

                String eventId = v.getTag(R.string.id).toString();
                loadEventMembers(eventId,null,"accepted");

                FrameLayout parentThatHasBottomSheetBehavior = (FrameLayout) mRecyclerViewFriend.getParent().getParent();
                mBottomSheetBehavior = BottomSheetBehavior.from(parentThatHasBottomSheetBehavior);
                if (mBottomSheetBehavior != null) {
                    // setStateText(mBottomSheetBehavior.getState());
                    mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {


                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            //  setOffsetText(slideOffset);
                        }
                    });
                }

                View peakView = findViewById(R.id.drag_me);
                mBottomSheetBehavior.setPeekHeight(peakView.getHeight());
                // int state = mBottomSheetBehavior.getState();
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                peakView.requestLayout();

                break;
            }
     /*       case R.id.event_btn_update:{

                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();



             *//*   Toast.makeText(this, "event update", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialogue_event_update, null);
                dialogBuilder.setView(dialogView);

              //  EditText editText = (EditText) dialogView.findViewById(R.id.label_field);
               // editText.setText("test label");

                TextView textView = dialogView.findViewById(R.id.event_date_tv);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerFragment timePickerFragment = new TimePickerFragment();
                        timePickerFragment.time(DisplayEventListActivity.this, textView);
                        timePickerFragment.show(DisplayEventListActivity.this.getFragmentManager(), "TimePicker");
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();*//*

                break;
            }*/
            case R.id.event_btn_delete:{

                String event_id = v.getTag(R.string.id).toString();
                deleteEvent(event_id);


                break;
            }
        }




    }






    @Override
    public void onItemClick(View view, int position) {


    }



    @Override
    public void onSetDate(String status,int month, int dayOfMonth, int year, String date, TextView textview) {

    }

    @Override
    public void onSetTimeBtnClick(String status,int hourOfDay, int minutes, String time, TextView textView) {

    }
}
