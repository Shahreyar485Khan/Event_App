package com.example.eventapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Activities.ParentActivity;
import com.example.eventapp.Activities.SignUp;
import com.example.eventapp.Interfaces.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhpMethodsUtils {

    private Context mCtx;

    //method to load all the devices from database
    public PhpMethodsUtils(Context mCtx) {
        this.mCtx = mCtx;

    }

    public static String currentDeviceId;
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
                                    currentDeviceId = d.getString("id");
                                }

                                Toast.makeText(mCtx, "" + currentDeviceId, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(mCtx, "" + error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", Constants.currentUser.getEmail());
                return params;
            }
        };
        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);
    }







    public void retrieveDataFromServer(String email, final ResponseCallback callback) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_GET_ID_BY_EMAIL,
                response -> {


                    JSONArray obj = null;
                    try {

                        obj = new JSONArray(response);
                        String str  = obj.getString(0);
                        callback.onSuccess(str);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);

    }







    public Boolean getIdFromEmail(String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_GET_ID_BY_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("phpMethodsUtils", response);
                        JSONArray obj = null;
                        try {

                            obj = new JSONArray(response);
                            String  id = obj.getString(0);
                            SharedPreferenceManager.getInstance(mCtx).saveRecipientID(id);
                            Log.d("phpMethodsUtils", id);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);
        return true;
    }



    public Boolean getNameFromID(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_GET_NAME_BY_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("phpMethodsUtils", response);
                        JSONArray obj = null;
                        try {
                            obj = new JSONArray(response);
                            String name = null;
                            name = obj.getString(0);
                          SharedPreferenceManager.getInstance(mCtx).saveSenderName(name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender", currentDeviceId);
                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);

        return true;
    }


    public void getNameFromId(final ResponseCallback callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_GET_NAME_BY_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("phpMethodsUtils", response);
                        JSONArray obj = null;
                        try {
                            obj = new JSONArray(response);
                            String name = null;
                            name = obj.getString(0);
                            callback.onSuccess(name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender", currentDeviceId);
                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);

    }


    public void sendRequestSharedPref(String recipientId, String email, String name) {

        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.d("phpMethodsUtils", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String senderName = SharedPreferenceManager.getInstance(mCtx).getSenderName();
                String recipientID = SharedPreferenceManager.getInstance(mCtx).getRecipientID();
                params.put("sender", currentDeviceId);
                params.put("recipient", recipientID);
                params.put("email", email);
                params.put("name", senderName);

                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);


    }


    public void sendRequestCallBack(String recipientId, String email, String name) {

        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        getNameFromId(new ResponseCallback() {
            @Override
            public void onSuccess(String response) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_REQUEST,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();

                                Log.d("phpMethodsUtils", response);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Log.d("errorOnerrorRes",error.getMessage());
                                Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("sender", currentDeviceId);
                        params.put("recipient",recipientId);
                        params.put("email", email);
                        params.put("name", response);

                        return params;
                    }
                };

                MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);

            }
        });


    }


    public void acceptRequest(String sender_id, String req_status, String email, String name) {

        progressDialog = new ProgressDialog(mCtx);

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_ACCEPT_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, "accepted dgdgdrr", Toast.LENGTH_LONG).show();
                        Log.d("phpMethodUtils", response);
                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(mCtx, "accepted" + obj.getString("message"), Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender", sender_id);
                params.put("recipient", currentDeviceId);
                params.put("req_status", req_status);
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


    public void acceptEventInvitation(String event_id,String sender_id, String req_status, String email, String name) {

        progressDialog = new ProgressDialog(mCtx);

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_ACCEPT_EVENT_INVITATION+"event_id="+event_id+"&sender_id="+sender_id+"&recipient_id="+currentDeviceId+"&email="+email+"&name="+name+"&event_req_status="+req_status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(mCtx, "accepted dgdgdrr", Toast.LENGTH_LONG).show();
                        Log.d("phpMethodUtils", response);
                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(mCtx, "accepted" + obj.getString("message"), Toast.LENGTH_LONG).show();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender", sender_id);
                params.put("recipient", currentDeviceId);
                params.put("req_status", req_status);
                params.put("email", email);
                params.put("name", name);

//                if (!TextUtils.isEmpty(image))
//                    params.put("image", image);

//                params.put("req_status", "pending");
                return params;
            }*/
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);
    }



    public void addEvent( String title, String location, String description, String eventStartTime, String eventStartDate,String eventEndDate,String eventEndTime){

       // getCurrentDevice();
        progressDialog = new ProgressDialog(mCtx);

        progressDialog.setMessage("Sending Push");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_ADD_EVENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       progressDialog.dismiss();

                        //Toast.makeText(mCtx, response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("message");
                            Log.d("phpMethodUtilsjava" , response);
                            Toast.makeText(mCtx, response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  progressDialog.dismiss();
                        Toast.makeText(mCtx, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", currentDeviceId);
                params.put("title", title);
                params.put("location", location);
                params.put("description", description);
                params.put("event_start_time", eventStartTime);
                params.put("event_start_date", eventStartDate);
                params.put("event_end_date", eventEndDate);
                params.put("event_end_time", eventEndTime);
                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);

    }


}
