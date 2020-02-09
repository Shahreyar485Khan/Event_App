package com.example.eventapp.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.R;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendListDialogueFragment extends DialogFragment  implements FriendListAdapter.AdapterListener, FriendListAdapter.FriendListAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private FriendListAdapter friendListAdapter;
    private List<String> name;
    private ProgressDialog progressDialog;
    PhpMethodsUtils phpMethodsUtils;
    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view


        View v = inflater.inflate(R.layout.friend_list_layout, container, false);


        name = new ArrayList<>();

        mRecyclerView   =  getActivity().findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        friendListAdapter = new FriendListAdapter(this, this, getActivity());

        loadRegisteredDevices();

        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            if (getArguments().getBoolean("notAlertDialog")) {
                return super.onCreateDialog(savedInstanceState);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert Dialog");
        builder.setMessage("Alert Dialog inside DialogFragment");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Log.d("API123", "onCreate");

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


      /*  name = new ArrayList<>();

        phpMethodsUtils = new PhpMethodsUtils(getActivity());


        mRecyclerView =  getActivity().findViewById(R.id.recycler_view);
        //    mRecyclerView.setLayoutManager(new LinearLayoutManager(FriendListDialogueFragment.this));
        //setadapter
        friendListAdapter = new FriendListAdapter(this, this, getActivity());
        // mRecyclerView.setAdapter(friendListAdapter);
        //get your recycler view and populate it.
        loadRegisteredDevices();*/

    }

    @Override
    public void onStart() {
        super.onStart();


       /* name = new ArrayList<>();

        phpMethodsUtils = new PhpMethodsUtils(getActivity());


        mRecyclerView =  getActivity().findViewById(R.id.recycler_view);
        //    mRecyclerView.setLayoutManager(new LinearLayoutManager(FriendListDialogueFragment.this));
        //setadapter
        friendListAdapter = new FriendListAdapter(this, this, getActivity());
        // mRecyclerView.setAdapter(friendListAdapter);
        //get your recycler view and populate it.
        loadRegisteredDevices();*/

    }


    @Override
    public void onClick(String bookmarksStr) {

    }

    @Override
    public void btnOnClick(View v, int position) {
        if (v.getId() == R.id.send_btn) {
          Log.d("FrienList" ,"ffhuehfeifuerf");      }

    }




    private void loadRegisteredDevices() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Devices...");
        progressDialog.show();

        EndPoints endPoints = new EndPoints(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, endPoints.URL_FETCH_DEVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("FriendList",response);
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("devices");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
//                                    devices.add(d.getString("email"));
                                    name.add(d.getString("first_name")+" "+d.getString("last_name"));
                                   // devices.add(d.getString("email"));
                                  //  id.add(d.getString("id"));

                                }

//                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                                        getActivity(),
//                                        android.R.layout.simple_spinner_item,
//                                        devices);

//                                listView.setAdapter(arrayAdapter);
                                friendListAdapter.setNamesList(name);
                              //  searchAdapter.setLocationList(devices);
//                                searchAdapter.setIdList(id);
                                mRecyclerView.setAdapter(friendListAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {

        };
        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }




}
