package com.example.eventapp.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventapp.ActivitySendPushNotification;
import com.example.eventapp.BaseActivity;
import com.example.eventapp.R;
import com.example.eventapp.Utils.TextUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignIn";
    private FirebaseAuth mAuth;

    private EditText username, password;
    private Button signInBtn, signOutBtn;
    private String signInUsernameStr, signInPasswordStr;
    private ProgressDialog progressDialog;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.sign_in_user_name_et);
        password = findViewById(R.id.sign_in_password_et);
        signInBtn = findViewById(R.id.sign_in_btn);
        signOutBtn =findViewById(R.id.sign_in_out_btn);
        forgetPassword =findViewById(R.id.txt_forget);
        progressDialog = new ProgressDialog(this);

        signInBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
//        if (!validateForm()) {
//            return;
//        }

//        showProgressBar();

        // [START sign_in_with_email]

        email = email.trim();
        password = password.trim();

        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            Toast.makeText(SignIn.this, "Signed In: "+user,
//                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignIn.this, FragmentsHolderActivity.class);
                            startActivity(intent);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
                        }
//                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.sign_in_btn)
        {
            preSignIn();
        }
        else if (id == R.id.sign_in_out_btn)
        {
            signOut();
        }
        else if (id == R.id.txt_forget)
        {
           String email = username.getText().toString();
           forgetPassword(email);
        }
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

    }

    private void forgetPassword(String email) {
        // Firebase sign out

        if (TextUtils.isNotEmpty(username)) {

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignIn.this, "Reset Email Sent.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });

        }else {
            Toast.makeText(SignIn.this, "Please Enter Email !!!", Toast.LENGTH_SHORT).show();
            username.setError("This field cannot be empty");

        }


    }


    private void preSignIn() {

        signInUsernameStr = username.getText().toString();
        signInPasswordStr = password.getText().toString();

        signIn(signInUsernameStr, signInPasswordStr);

    }
}
