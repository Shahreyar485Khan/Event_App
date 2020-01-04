package com.example.eventapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Activities.ParentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhpMethodsUtils {

    private Context mCtx;

    //method to load all the devices from database
    public PhpMethodsUtils(Context mCtx)
    {
        this.mCtx = mCtx;

    }

    private static String currentDeviceId;
    private static String recipientId;

    private ProgressDialog progressDialog;

    public void getCurrentDevice() {

        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Fetching Devices...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_FETCH_CURRENT_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("devices");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
//                                    devices.add(d.getString("email"));
//                                    name.add(d.getString("id"));
                                    currentDeviceId = d.getString("id");
                                }

                                Toast.makeText(mCtx, ""+currentDeviceId, Toast.LENGTH_SHORT).show();

//                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                                        this,
//                                        android.R.layout.simple_spinner_item,
//                                        devices);
//
////                                listView.setAdapter(arrayAdapter);
//                                searchAdapter.setNamesList(name);
//                                recyclerView.setAdapter(searchAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mCtx, ""+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", Constants.currentUser.getEmail());
//                params.put("recipient", message);

//                if (!TextUtils.isEmpty(image))
//                    params.put("image", image);

//                params.put("req_status", "pending");
                return params;
            }
        };
        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);
    }


    public void sendRequest(String recipientId, String email, String name){

        progressDialog = new ProgressDialog(mCtx);

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Toast.makeText(mCtx, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender", currentDeviceId);
                params.put("recipient", recipientId);
                params.put("email", email);
                params.put("name", name);

//                if (!TextUtils.isEmpty(image))
//                    params.put("image", image);

//                params.put("req_status", "pending");
                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);
    }


}
