package com.example.eventapp.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Interfaces.ResponseCallback;
import com.example.eventapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
        progressDialog.setMessage("Fetching user data...");
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

                            //    Toast.makeText(mCtx, "" + currentDeviceId, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        if (error instanceof NetworkError) {
                            Toast.makeText(mCtx,
                                    "Oops. NetworkError error!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(mCtx,
                                    "Oops. ServerError error!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(mCtx,
                                    "Oops. AuthFailureError error!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(mCtx,
                                    "Oops. ParseError error!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(mCtx,
                                    "Oops. NoConnectionError error!",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(mCtx,
                                    "Oops. Timeout error!",
                                    Toast.LENGTH_LONG).show();
                        }

                    //    Toast.makeText(mCtx, "" + error, Toast.LENGTH_SHORT).show();
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
                        String str = obj.getString(0);
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
                            String id = obj.getString(0);
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


    public Boolean getNameFromID() {


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


    public void sendRequest(String id, String email, String name) {

        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Sending Pushed..");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_SEND_REQUEST_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.d("sendrequestmethod", response);

                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response);
                            String str = obj.getString("message");
                            Toast.makeText(mCtx, "Response" + str, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                       /* JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                           // if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("response");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);

                                    Log.d("sendrequestmethod", d.getString("message"));
                                    Log.d("sendrequestmethod", d.getString("message"));

                                  //  currentDeviceId = d.getString("id");
                                }

                              //  Toast.makeText(mCtx, "" + currentDeviceId, Toast.LENGTH_SHORT).show();
                           // }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
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
                //  String senderName = SharedPreferenceManager.getInstance(mCtx).getSenderName();
                //  String recipientID = SharedPreferenceManager.getInstance(mCtx).getRecipientID();
                params.put("sender", currentDeviceId);
                params.put("recipient", id);
                params.put("email", email);
                params.put("name", name);

                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);


    }


    public void sendRequestCallBack(String recipientId, String email, String name) {

        progressDialog = new ProgressDialog(mCtx);
        progressDialog.setMessage("Sending Pushes...");
        progressDialog.show();

        getNameFromId(new ResponseCallback() {
            @Override
            public void onSuccess(String response) {

                StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_SEND_REQUEST + "sender=" + currentDeviceId + "&recipient=" + recipientId + "&email=" + email + "&name=" + response,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();

                                Log.d("sendRequestCallBack", response);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Log.d("errorOnerrorRes", error.getMessage());
                                Toast.makeText(mCtx, "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                  /*  @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("sender", currentDeviceId);
                        params.put("recipient",recipientId);
                        params.put("email", email);
                        params.put("name", response);

                        return params;
                    }*/
                };

                MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);

            }
        });


    }

    boolean server_result;

    public boolean acceptRequest(String sender_id, String req_status, String email, String name) {

        server_result = false;
        progressDialog = new ProgressDialog(mCtx);

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_ACCEPT_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                       // Toast.makeText(mCtx, "accepted dgdgdrr", Toast.LENGTH_LONG).show();
                        Log.d("phpMethodUtils", response);
                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response);
                            String str = obj.getString("message");
                         //   Toast.makeText(mCtx, "Response" + str, Toast.LENGTH_SHORT).show();

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
                params.put("sender", sender_id);
                params.put("recipient", currentDeviceId);
                params.put("req_status", req_status);
                params.put("email", email);
                params.put("name", name);

                return params;
            }
        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);
        return server_result;
    }


    public boolean acceptEventInvitation(String event_id, String sender_id, String req_status, String email, String name) {

        server_result = false;
        progressDialog = new ProgressDialog(mCtx);

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_ACCEPT_EVENT_INVITATION + "event_id=" + event_id + "&sender_id=" + sender_id + "&recipient_id=" + currentDeviceId + "&email=" + email + "&name=" + name + "&event_req_status=" + req_status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.d("phpMethodUtils", response);
                        JSONObject obj = null;

                        try {
                            obj = new JSONObject(response);
                            String str = obj.getString("message");

                            if (str.equals(mCtx.getString(R.string.resp_successful))){

                                server_result = true;
                            }

                         //   Toast.makeText(mCtx, "Response" + str, Toast.LENGTH_SHORT).show();

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

        };

        MyVolley.getInstance(mCtx).addToRequestQueue(stringRequest);
        return server_result;
    }


    public void addEvent(String currentId,String title, String location, String description, String eventStartTime, String eventStartDate, String eventEndDate, String eventEndTime) {

        // getCurrentDevice();
     //   server_result = false;
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
                            Log.d("phpMethodUtilsjava", response);
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
                params.put("user_id", currentId);
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
