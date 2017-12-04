package com.foxbrajcich.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {

    public static final String PREFERENCES_FILE = "user_session_storage";

    private FirebaseDatabase mFirebaseDatabase;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private boolean attemptingLogin = false;
    private boolean attemptingRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        String storedName =  preferences.getString("name", "");
        String storedUsername = preferences.getString("username", "");

        if(storedName != "" && storedUsername != ""){
            UserSession.setName(storedName);
            UserSession.setUsername(storedUsername);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameField = (EditText) findViewById(R.id.nameTextView);
                if(nameField.getText().toString().equals("")){
                    //show name field
                    nameField.setVisibility(VISIBLE);
                }else{
                    //register the user
                    attemptRegister();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptLogin() {
        if (attemptingLogin) {
            return;
        }

        attemptingLogin = true;

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();

        showProgress(true);

        final ValueEventListener newListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //the user was found in the table
                    Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();
                    String name = (String) userData.get("name");

                    logUserIn(name, username);

                }else{
                    //the user does not exist in the table
                    showProgress(false);
                    mUsernameView.setError("Username is not registered");
                    attemptingLogin = false;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showProgress(false);
                attemptingLogin = false;
            }
        };

        final DatabaseReference childRef = mFirebaseDatabase.getReference("users").child(username);
        childRef.addValueEventListener(newListener);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                childRef.removeEventListener(newListener);
                attemptingLogin = false;
                showProgress(false);
            }
        }, 15000);

    }

    private void attemptRegister(){
        if(attemptingRegister){
            return;
        }

        attemptingRegister = true;

        final String username = mUsernameView.getText().toString();
        final String name = ((EditText) findViewById(R.id.nameTextView)).getText().toString();

        showProgress(true);

        Handler timeoutEvent = new Handler();

        final ValueEventListener newListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //the user already exists
                    mUsernameView.setError("Username already in use");
                    showProgress(false);
                    attemptingRegister = false;

                }else{
                    //the user does not exist in the table. Register them and log them in
                    showProgress(false);
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("name", name);

                    DatabaseReference userLocation = mFirebaseDatabase.getReference("users").child(username);
                    userLocation.setValue(userData);

                    logUserIn(name, username);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showProgress(false);
                attemptingRegister = false;
            }
        };

        final DatabaseReference childRef = mFirebaseDatabase.getReference("users").child(username);
        childRef.addValueEventListener(newListener);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                childRef.removeEventListener(newListener);
                attemptingRegister = false;
                showProgress(false);
            }
        }, 15000);

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? GONE : VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? GONE : VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? VISIBLE : GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? VISIBLE : GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? VISIBLE : GONE);
            mLoginFormView.setVisibility(show ? GONE : VISIBLE);
        }
    }

    private void logUserIn(String name, String username){
        UserSession.setName(name);
        UserSession.setUsername(username);

        //save the user's login to shared preferences
        SharedPreferences preferences = LoginActivity.this.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", UserSession.getName());
        editor.putString("username", UserSession.getUsername());
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                UserSession.setName(mEmail);
//                UserSession.setUsername(mEmail);
//
//                //save the user's login to shared preferences
//                SharedPreferences preferences = LoginActivity.this.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("name", UserSession.getName());
//                editor.putString("username", UserSession.getUsername());
//                editor.commit();
//
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }

}

