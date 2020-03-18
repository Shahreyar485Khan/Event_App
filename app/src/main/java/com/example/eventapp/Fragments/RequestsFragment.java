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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Adapters.RequestAdapter;
import com.example.eventapp.R;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.example.eventapp.Utils.StringFormat;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RequestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsFragment extends Fragment implements RequestAdapter.AdapterListener, RequestAdapter.RequestAdapterOnClickHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private RequestAdapter requestAdapter;
    private TextView txtEmpty;
    private InterstitialAd interstitialAd;

    public RequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestsFragment newInstance(String param1, String param2) {
        RequestsFragment fragment = new RequestsFragment();
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
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    PhpMethodsUtils phpMethodsUtils;

    @Override
    public void onStart() {
        super.onStart();

        senderName = new ArrayList<>();
        senderEmail = new ArrayList<>();
        id = new ArrayList<>();
        recipientName = new ArrayList<>();

        phpMethodsUtils = new PhpMethodsUtils(getActivity());

        recyclerView = getActivity().findViewById(R.id.requests_recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        txtEmpty = getActivity().findViewById(R.id.empty_text);
        requestAdapter = new RequestAdapter(this, this, getActivity());

        reqNewInterstitial();
        getPendingRequestList("pending");



//        devices = new ArrayList<>();
//        id = new ArrayList<>();
//        name = new ArrayList<>();
//
//        phpMethodsUtils = new PhpMethodsUtils(getActivity());
//
////        listView = getActivity().findViewById(R.id.listView);
//
//        recyclerView = getActivity().findViewById(R.id.recycler_view);
//        searchAdapter = new SearchAdapter(this, this, getActivity());

//        loadRegisteredDevices();


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
    public void onClick(String bookmarksStr) {

    }

    @Override
    public void btnOnClick(View v, int position) {

       /* if (v.getId() == R.id.confirm_btn) {
            readRequests("accepted");
        } else if (v.getId() == R.id.reject_btn) {
            readRequests("rejected");
        }*/

        String email = v.getTag(R.string.sender_email).toString();
        String name = v.getTag(R.string.sender_name).toString();
        String id = v.getTag(R.string.sender_id).toString();



        if (v.getId() == R.id.confirm_btn) {

            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                reqNewInterstitial();
                phpMethodsUtils.acceptRequest(id, "accepted",email, name);

            }
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    reqNewInterstitial();
                    phpMethodsUtils.acceptRequest(id, "accepted",email, name);
                }
            });



        } else if (v.getId() == R.id.reject_btn) {

            if (interstitialAd.isLoaded()) {
                interstitialAd.show();
            } else {
                reqNewInterstitial();
                phpMethodsUtils.acceptRequest(id,"rejected",email,name);

            }
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    reqNewInterstitial();
                    phpMethodsUtils.acceptRequest(id,"rejected",email,name);
                }
            });





        }


    }

    public void reqNewInterstitial() {
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId(getResources().getString(R.string.Interstitial));
        interstitialAd.loadAd(new AdRequest.Builder().build());
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

    private List<String> senderName;
    private List<String> senderEmail;
    private List<String> id;
    private List<String> recipientName;
    //    private List<String> name;
    private ProgressDialog progressDialog;

    public void readRequests(String reqStatus) {

        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Sending Push");
        progressDialog.show();

        //Toast.makeText(getActivity(), "out", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.GET_REQUEST_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                JSONArray jsonDevices = obj.getJSONArray("requests");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
//                                    devices.add(d.getString("email"));
                                    senderName.add(d.getString("sendername"));
                                    recipientName.add(d.getString("recipientname"));
//                                    id.add(d.getString("id"));

                                }

//                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                                        getActivity(),
//                                        android.R.layout.simple_spinner_item,
//                                        devices);

//                                listView.setAdapter(arrayAdapter);
                                requestAdapter.setSenderList(senderName);
                                requestAdapter.setRecipientList(recipientName);
//                                searchAdapter.setIdList(id);
                                recyclerView.setAdapter(requestAdapter);
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("req_status", reqStatus);

                return params;
            }
        };

        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }



    public void getPendingRequestList(String reqStatus) {

        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Loading request list...");
        progressDialog.show();

        //Toast.makeText(getActivity(), "out", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_GET_PENDING_REQUEST_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                //Toast.makeText(getActivity(), "IN", Toast.LENGTH_SHORT).show();
                                JSONArray jsonDevices = obj.getJSONArray("requests");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
//                                    devices.add(d.getString("email"));
                                    String sender_name = d.getString("sendername");
                                    sender_name = StringFormat.removebrakets(sender_name);
                                    sender_name = StringFormat.removeQoutes(sender_name);

                                    String sender_email = d.getString("senderemail");
                                    sender_email = StringFormat.removebrakets(sender_email);
                                    sender_email = StringFormat.removeQoutes(sender_email);

                                    senderName.add(sender_name);
                                    id.add(d.getString("senderid"));
                                    senderEmail.add(sender_email);
                                    //recipientName.add(d.getString("recipientname"));
//                                    id.add(d.getString("id"));

                                }

                                Log.d("RequestFragment", "check "+senderName);
//
                                requestAdapter.setSenderList(senderName);
                                requestAdapter.setIdList(id);
                                requestAdapter.setEmailList(senderEmail);
                               // requestAdapter.setRecipientList(recipientName);
                               // searchAdapter.setIdList(id);
                                recyclerView.setAdapter(requestAdapter);


                                if (requestAdapter.getItemCount() == 0){

                                    recyclerView.setVisibility(View.GONE);
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
                            Toast.makeText(getActivity(), "errorr", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("RequestFragment", "currentid "+ PhpMethodsUtils.currentDeviceId);
                params.put("req_status", reqStatus);
                params.put("recipient_id", PhpMethodsUtils.currentDeviceId);

                return params;
            }
        };

        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
