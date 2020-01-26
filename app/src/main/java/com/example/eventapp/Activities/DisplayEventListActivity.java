package com.example.eventapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Adapters.AllEventListAdapter;
import com.example.eventapp.Adapters.FriendListAdapter;

import com.example.eventapp.R;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.example.eventapp.Utils.StringFormat;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eventapp.Utils.PhpMethodsUtils.currentDeviceId;

public class DisplayEventListActivity extends AppCompatActivity implements FriendListAdapter.AdapterListener, FriendListAdapter.FriendListAdapterOnClickHandler,AllEventListAdapter.ItemClickListener,AllEventListAdapter.AdapterListener{


    AllEventListAdapter adapter;
    Button btnSendInvites;
    private ArrayList<String> titleList;
    private List<String> locationList;
    private List<String> event_idList;
    private List<String> descList;
    private List<String> st_timeList;
    private List<String> end_timeList;
    private List<String> st_dateList;
    private List<String> end_dateList;






    private ArrayList<String> friendList;
    private RecyclerView mRecyclerViewEvent;
    private RecyclerView mRecyclerViewFriend;
    private AllEventListAdapter allEventListAdapter;
    private FriendListAdapter friendListAdapter;
    private ProgressDialog progressDialog;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_event_list);


        /*Fragment fragment=new EventListFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment,fragment).commit();*/


        event_idList = new ArrayList<>();
        titleList = new ArrayList<>();
        locationList = new ArrayList<>();
        descList = new ArrayList<>();
        st_timeList = new ArrayList<>();
        end_timeList = new ArrayList<>();
        st_dateList = new ArrayList<>();
        end_dateList = new ArrayList<>();



        friendList = new ArrayList<>();

        mRecyclerViewEvent = findViewById(R.id.recycler_view_events);
        mRecyclerViewFriend = findViewById(R.id.recyclerview_bottom);

        mRecyclerViewEvent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerViewFriend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadAllEvents();

        allEventListAdapter = new AllEventListAdapter(this, titleList,this);

        loadAcceptedRequestDevices();

       friendListAdapter = new FriendListAdapter(this, this,this);



        FrameLayout parentThatHasBottomSheetBehavior = (FrameLayout) mRecyclerViewFriend.getParent().getParent();
        mBottomSheetBehavior = BottomSheetBehavior.from(parentThatHasBottomSheetBehavior);
        if (mBottomSheetBehavior != null) {
           // setStateText(mBottomSheetBehavior.getState());
            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                  //  setStateText(newState);
/*
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }*/


                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                  //  setOffsetText(slideOffset);
                }
            });
        }



    }



    private void loadAllEvents() {

        Toast.makeText(this, "currrent id"+currentDeviceId, Toast.LENGTH_SHORT).show();

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

      /*  progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("getting Devices...");
        progressDialog.show();
*/

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

                                    String sender_name = d.getString("sendername");
                                    sender_name = StringFormat.removebrakets(sender_name);
                                    sender_name = StringFormat.removeQoutes(sender_name);

                                    friendList.add(sender_name);
                                }


                                friendListAdapter.setNamesList(friendList);
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

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("req_status", "accepted");
                params.put("recipient_id", PhpMethodsUtils.currentDeviceId);
                return params;
            }


        };
        MyVolley.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(String bookmarksStr) {

    }

    @Override
    public void btnOnClick(View v, int position) {

      //  Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

        switch (v.getId()){

            case R.id.event_btn_send:{
                Toast.makeText(this, "send btn", Toast.LENGTH_SHORT).show();
                View peakView = findViewById(R.id.drag_me);
                mBottomSheetBehavior.setPeekHeight(peakView.getHeight());
               // int state = mBottomSheetBehavior.getState();
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                peakView.requestLayout();
                break;
            }
            case R.id.send_btn_friend:{

                Toast.makeText(this, "send event", Toast.LENGTH_SHORT).show();
                break;
            }


        }




    }

    @Override
    public void onItemClick(View view, int position) {


    }
}
