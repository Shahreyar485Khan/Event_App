package com.example.eventapp;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;
import android.widget.Toast;

import com.example.eventapp.Utils.SharedPrefManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends BaseActivity {

    Context mContext = this;

    private Toolbar toolbar;
    private TextView mFullNameTextView, mEmailTextView;
    private CircleImageView mProfileImageView;
    private String mUsername, mEmail;

    SharedPrefManager sharedPrefManager;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mFullNameTextView = findViewById(R.id.full_name);
        mEmailTextView = findViewById(R.id.email);
        mProfileImageView = findViewById(R.id.profile_image);

//        // create an object of sharedPreferenceManager and get stored user data
        sharedPrefManager = new SharedPrefManager(mContext);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();
        String uri = sharedPrefManager.getPhoto();
        Uri mPhotoUri = Uri.parse(uri);

        mFullNameTextView.setText(mUsername);
        mEmailTextView.setText(mEmail);
//
        Picasso.with(mContext)
                .load(mPhotoUri)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(mProfileImageView);

        //        configureSignIn();
    }

}