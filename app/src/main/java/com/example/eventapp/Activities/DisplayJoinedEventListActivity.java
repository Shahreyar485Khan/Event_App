package com.example.eventapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Adapters.InvitationAdapter;
import com.example.eventapp.Adapters.JoinedEventListAdapter;
import com.example.eventapp.R;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.example.eventapp.Utils.StringFormat;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayJoinedEventListActivity extends AppCompatActivity implements JoinedEventListAdapter.AdapterListener {


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

    TextView txtEmpty;


    private ProgressDialog progressDialog;

    PhpMethodsUtils phpMethodsUtils;

    private RecyclerView recyclerView;
    private JoinedEventListAdapter joinedEventListAdapter;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joined_event_list);

        event_idList = new ArrayList<>();
        titleList = new ArrayList<>();
        locationList = new ArrayList<>();
        descList = new ArrayList<>();
        st_timeList = new ArrayList<>();
        end_timeList = new ArrayList<>();
        st_dateList = new ArrayList<>();
        end_dateList = new ArrayList<>();

        txtEmpty = findViewById(R.id.empty_text);

        senderName = new ArrayList<>();
        senderEmail = new ArrayList<>();
        id = new ArrayList<>();


        phpMethodsUtils = new PhpMethodsUtils(this);


        recyclerView = findViewById(R.id.recycler_view_joined_events);

        getJoinedInvitationList("accepted");

        joinedEventListAdapter = new JoinedEventListAdapter(this, titleList, this);

        BannerAd();

    }


    public void getJoinedInvitationList(String reqStatus) {

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading events...");
        progressDialog.show();

        //Toast.makeText(getActivity(), "out", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_GET_PENDING_INVITATION_LIST + "recipient_id=" + PhpMethodsUtils.currentDeviceId + "&req_status=" + reqStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("EventInvitationFragment", response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                Toast.makeText(DisplayJoinedEventListActivity.this, "IN", Toast.LENGTH_SHORT).show();
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

                                    Log.d("eventid", "event id    " + event_id + "   event title " + event_title);


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


                                Log.d("RequestFragment", "check " + senderName);
                                joinedEventListAdapter.setSenderList(senderName);
                                joinedEventListAdapter.setIdList(id);
                                joinedEventListAdapter.setEmailList(senderEmail);

                                joinedEventListAdapter.setEventIdList(event_idList);
                                joinedEventListAdapter.setTitleList(titleList);
                                joinedEventListAdapter.setLocationList(locationList);
                                joinedEventListAdapter.setDescList(descList);
                                joinedEventListAdapter.setSt_timeList(st_timeList);
                                joinedEventListAdapter.setEnd_timeList(end_timeList);
                                joinedEventListAdapter.setSt_dateList(st_dateList);
                                joinedEventListAdapter.setEnd_dateList(end_dateList);


                                recyclerView.setAdapter(joinedEventListAdapter);


                                if (joinedEventListAdapter.getItemCount() == 0){

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
                        Toast.makeText(DisplayJoinedEventListActivity.this, "errorr", Toast.LENGTH_SHORT).show();
                    }
                }) {

        };

        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void BannerAd() {
        adView = findViewById(R.id.bannerad);
        AdRequest adrequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adrequest);
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int error) {
                adView.setVisibility(View.GONE);
            }

        });
    }



    @Override
    public void btnOnClick(View v, int position) {

        switch (v.getId()) {

            case R.id.event_btn_leave: {

                String sender_name = v.getTag(R.string.sender_name).toString();
                String sender_email = v.getTag(R.string.sender_email).toString();
                String event_id = v.getTag(R.string.event_id).toString();
                String user_id = v.getTag(R.string.sender_id).toString();

                Toast.makeText(this, "btn leave", Toast.LENGTH_SHORT).show();
                phpMethodsUtils.acceptEventInvitation(event_id,user_id, "rejected",sender_email, sender_name);


            }

        }

    }
}
