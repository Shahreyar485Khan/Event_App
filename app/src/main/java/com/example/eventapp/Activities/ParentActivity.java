package com.example.eventapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.invoke.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.eventapp.Fragments.CalendarFragment;
import com.example.eventapp.Fragments.EventFragment;
import com.example.eventapp.Fragments.RequestsFragment;
import com.example.eventapp.Fragments.SearchFragment;
import com.example.eventapp.R;
import com.example.eventapp.Utils.Constants;
import com.example.eventapp.Utils.EndPoints;
import com.example.eventapp.Utils.MyVolley;
import com.example.eventapp.Utils.PhpMethodsUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParentActivity extends AppCompatActivity implements CalendarFragment.OnFragmentInteractionListener,
        EventFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener, RequestsFragment.OnFragmentInteractionListener {

    private FirebaseAuth mAuth;
    private List<String> name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);


        PhpMethodsUtils phpMethodsUtils = new PhpMethodsUtils(this);

        Fragment fragment=new CalendarFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment,fragment).commit();

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        CalendarFragment fragmentDemo = CalendarFragment.newInstance("", "my title");
//        ft.replace(R.id.fragment, fragmentDemo);
//        ft.commit();

        mAuth = FirebaseAuth.getInstance();

        Constants.currentUser = mAuth.getCurrentUser();
//
//        Toast.makeText(this, ""+Constants.currentUser.getEmail(), Toast.LENGTH_SHORT).show();
        name = new ArrayList<>();

        phpMethodsUtils.getCurrentDevice();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

//        Toast.makeText(this, "Interacted", Toast.LENGTH_SHORT).show();
    }

}
