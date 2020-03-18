package com.example.eventapp.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Adapters.EventListAdapter;
import com.example.eventapp.Adapters.FriendListDialogueFragment;
import com.example.eventapp.Adapters.InvitationAdapter;
import com.example.eventapp.Adapters.SearchAdapter;
import com.example.eventapp.Interfaces.ResponseCallback;
import com.example.eventapp.R;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.eventapp.Utils.PhpMethodsUtils.currentDeviceId;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends Fragment implements EventListAdapter.AdapterListener, EventListAdapter.EventListAdapterOnClickHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static final String TAG = "EventListFragment";

    private List<String> devices;
    private List<String> id;
    private List<String> name;
    private List<String> lastName;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private ListView listView;
    private RecyclerView recyclerView;
    EventListAdapter eventListAdapter;
    PhpMethodsUtils phpMethodsUtils;

    public EventListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventListFragment newInstance(String param1, String param2) {
        EventListFragment fragment = new EventListFragment();
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
        return inflater.inflate(R.layout.fragment_event_list, container, false);
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

        devices = new ArrayList<>();
        id = new ArrayList<>();
        name = new ArrayList<>();

        phpMethodsUtils = new PhpMethodsUtils(getActivity());

//        listView = getActivity().findViewById(R.id.listView);

        recyclerView = getActivity().findViewById(R.id.recycler_view);
        eventListAdapter = new EventListAdapter(this, this, getActivity());

        loadAllEvents();

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

    String temp_id = null;
    @Override
    public void btnOnClick(View v, int position) {

       // String email = v.getTag(R.string.email).toString();
        String name = v.getTag(R.string.name).toString();
       // String id = v.getTag(R.string.id).toString();




      /*  FriendListDialogueFragment  nextFrag= new FriendListDialogueFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
*/


        Toast.makeText(getActivity(), "event list", Toast.LENGTH_SHORT).show();

       /* FriendListDialogueFragment dialogFragment = new FriendListDialogueFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);

        dialogFragment.setArguments(bundle);

       FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);


        dialogFragment.show(ft, "dialog");*/



/*
        Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(R.layout.friend_list_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();*/



       // FriendListDialogueFragment dialog = new FriendListDialogueFragment;
        //dialog.show(getActivity().getFragmentManager(), "MyDialogFragment");







       /* phpMethodsUtils.retrieveDataFromServer(email,new ResponseCallback(){
            @Override
            public void onSuccess(String response){
                //Get result from here
                temp_id = response;
                Toast.makeText(getActivity(), "id " + response + "email " + email, Toast.LENGTH_SHORT).show();

                phpMethodsUtils.sendRequestCallBack(String.valueOf(temp_id), email, name);

            }
        });*/


    }






    private void loadAllEvents() {

      //  Toast.makeText(getActivity(), "currrent id"+currentDeviceId, Toast.LENGTH_SHORT).show();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading events...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.URL_GET_TEST_ALL_EVENT+currentDeviceId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        Log.d(TAG , "get_all_events "+response);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                JSONArray jsonDevices = obj.getJSONArray("events");

                                for (int i = 0; i < jsonDevices.length(); i++) {
                                    JSONObject d = jsonDevices.getJSONObject(i);
//                                    devices.add(d.getString("email"));
                                    name.add(d.getString("title"));
                                   // devices.add(d.getString("email"));
                                   // id.add(d.getString("id"));

                                }

//                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                                        getActivity(),
//                                        android.R.layout.simple_spinner_item,
//                                        devices);

//                                listView.setAdapter(arrayAdapter);
                                eventListAdapter.setNamesList(name);
                                //eventListAdapter.setLocationList(devices);
//                                searchAdapter.setIdList(id);
                                recyclerView.setAdapter(eventListAdapter);
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

           /* @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sender1", "4");
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
