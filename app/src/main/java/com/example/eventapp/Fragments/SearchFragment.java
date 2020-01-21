package com.example.eventapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.ActivitySendPushNotification;
import com.example.eventapp.Adapters.SearchAdapter;
import com.example.eventapp.Interfaces.ResponseCallback;
import com.example.eventapp.R;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.example.eventapp.Utils.SharedPreferenceManager;

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
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchAdapter.SearchAdapterOnClickHandler, SearchAdapter.AdapterListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<String> devices;
    private List<String> id;
    private List<String> name;
    private List<String> lastName;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private ListView listView;
    private RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    PhpMethodsUtils phpMethodsUtils;

    String rec_id=null;

    public SearchFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
}

    @Override
    public void onStart() {
        super.onStart();

        devices = new ArrayList<>();
        id = new ArrayList<>();
        name = new ArrayList<>();

        phpMethodsUtils = new PhpMethodsUtils(getActivity());

//        listView = getActivity().findViewById(R.id.listView);

        recyclerView = getActivity().findViewById(R.id.recycler_view);
        searchAdapter = new SearchAdapter(this, this, getActivity());

        loadRegisteredDevices();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    //method to load all the devices from database
    private void loadRegisteredDevices() {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Devices...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_FETCH_DEVICES,
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
                                    name.add(d.getString("first_name")+" "+d.getString("last_name"));
                                    devices.add(d.getString("email"));
                                    id.add(d.getString("id"));

                                }

//                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                                        getActivity(),
//                                        android.R.layout.simple_spinner_item,
//                                        devices);

//                                listView.setAdapter(arrayAdapter);
                                searchAdapter.setNamesList(name);
                                searchAdapter.setEmailList(devices);
//                                searchAdapter.setIdList(id);
                                recyclerView.setAdapter(searchAdapter);
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



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(String bookmarksStr) {

    }
    String temp_id = null;
    @Override
    public void btnOnClick(View v, int position) {

        String email = v.getTag(R.string.email).toString();
        String name = v.getTag(R.string.name).toString();

      //  phpMethodsUtils.getNameFromID();

      //  boolean isReceive = phpMethodsUtils.getIdFromEmail(email);

      //  phpMethodsUtils.sendRequest(String.valueOf(temp_id), email, name);
       /* if (isReceive){

           temp_id =  SharedPreferenceManager.getInstance(getActivity()).getRecipientID();

        }
        Toast.makeText(getActivity(), "id " + temp_id + "email " + email, Toast.LENGTH_SHORT).show();
*/


        phpMethodsUtils.retrieveDataFromServer(email,new ResponseCallback(){
            @Override
            public void onSuccess(String response){
                //Get result from here
                temp_id = response;
                Toast.makeText(getActivity(), "id " + response + "email " + email, Toast.LENGTH_SHORT).show();

                phpMethodsUtils.sendRequestCallBack(String.valueOf(temp_id), email, name);

            }
        });




        /* getIdFromEmail(email);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending Request...");
        progressDialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "id " + rec_id + "email " + email, Toast.LENGTH_SHORT).show();
                phpMethodsUtils.sendRequest(String.valueOf(id), email, name);
            }
        }, 1000);

*/


        if (id != null) {

            //Toast.makeText(getActivity(), "id " + id + "email " + email, Toast.LENGTH_SHORT).show();

         //   phpMethodsUtils.sendRequest(String.valueOf(id), email, name);
        }

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
       // void onFragmentInteraction(Uri uri);
    }







    public void getIdFromEmail(String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.URL_GET_ID_BY_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("SearchFragment", response);
                        JSONArray obj = null;
                        try {

                            obj = new JSONArray(response);
                            rec_id = obj.getString(0);
                            Log.d("SearchFragment", rec_id);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "error" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        MyVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }















}
