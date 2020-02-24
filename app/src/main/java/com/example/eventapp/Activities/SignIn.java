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
import android.widget.Toast;

import com.example.eventapp.ActivitySendPushNotification;
import com.example.eventapp.BaseActivity;
import com.example.eventapp.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.sign_in_user_name_et);
        password = findViewById(R.id.sign_in_password_et);
        signInBtn = findViewById(R.id.sign_in_btn);
        signOutBtn =findViewById(R.id.sign_in_out_btn);
        progressDialog = new ProgressDialog(this);

        signInBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);

    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
//        if (!validateForm()) {
//            return;
//        }

//        showProgressBar();

        // [START sign_in_with_email]
        progressDialog.show();
        mAuth.signInWithEmailAndPassword("sherry123@gmail.com", "1234567890")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignIn.this, "Signed In: "+user,
                                    Toast.LENGTH_SHORT).show();
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
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();

    }


    private void preSignIn() {

        signInUsernameStr = username.getText().toString();
        signInPasswordStr = password.getText().toString();

        signIn(signInUsernameStr, signInPasswordStr);

    }
}
