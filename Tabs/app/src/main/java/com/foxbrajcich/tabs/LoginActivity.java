package com.foxbrajcich.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.MutableBoolean;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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
    private boolean friendsListLoaded = false;
    private boolean groupsListLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //make dummy query to database to initialize connection
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference("users").child("").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //do nothing
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //do nothing
            }
        });

        UserSession.clearUserSession();

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);

        SharedPreferences preferences = this.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        String storedName =  preferences.getString("name", "");
        String storedUsername = preferences.getString("username", "");

        if(storedName != "" && storedUsername != ""){
            logUserIn(storedName, storedUsername);
        }

        // Set up the login form.
        mUsernameView = findViewById(R.id.username);

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

    }


    private void attemptLogin() {
        if (attemptingLogin) {
            return;
        }

        attemptingLogin = true;

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString().toLowerCase();

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
        childRef.addListenerForSingleValueEvent(newListener);

    }

    private void attemptRegister(){
        if(attemptingRegister){
            return;
        }

        attemptingRegister = true;

        final String username = mUsernameView.getText().toString().toLowerCase();
        final String name = ((EditText) findViewById(R.id.nameTextView)).getText().toString();

        final DatabaseReference childRef = mFirebaseDatabase.getReference("users").child(username);

        showProgress(true);

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

        childRef.addListenerForSingleValueEvent(newListener);

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
        showProgress(true);

        UserSession.setName(name);
        UserSession.setUsername(username);

        //save the user's login to shared preferences
        SharedPreferences preferences = LoginActivity.this.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", UserSession.getName());
        editor.putString("username", UserSession.getUsername());
        editor.commit();

        mFirebaseDatabase.getReference("users").child(username).child("friends").addListenerForSingleValueEvent(new FriendsListLoader());
        mFirebaseDatabase.getReference("Groups").addListenerForSingleValueEvent(new GroupsListLoader());
    }

    private void databaseQueriedCallback(){
        if(friendsListLoaded && groupsListLoaded) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class FriendsListLoader implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(!dataSnapshot.exists()){
                friendsListLoaded = true;
                databaseQueriedCallback();
                return;
            }

            Map<String, Object> usernames = (Map<String, Object>) dataSnapshot.getValue();

            for(String randomId : usernames.keySet()){
                UserSession.addFriendsUsername((String) usernames.get(randomId));
            }

            friendsListLoaded = true;
            databaseQueriedCallback();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private class GroupsListLoader implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(!dataSnapshot.exists()){
                groupsListLoaded = true;
                databaseQueriedCallback();
                return;
            }

            Map<String, Object> groups = (Map<String, Object>) dataSnapshot.getValue();

            for(String groupId : groups.keySet()){
                Map<String, Object> groupInfo = (Map<String, Object>) groups.get(groupId);
                String groupName = (String) groupInfo.get("name");

                Map<String, Object> expenses = (Map<String, Object>) groupInfo.get("expenses");
                Map<String, Object> members = (Map<String, Object>) groupInfo.get("members");
                Map<String, Object> transactions = (Map<String, Object>) groupInfo.get("transactions");

                List<Expense> expenseList = new ArrayList<>();
                List<User> memberList = new ArrayList<>();
                List<Transaction> transactionList = new ArrayList<>();

                if(expenses != null && expenses.size() > 0) {
                    for (String expenseId : expenses.keySet()) {
                        Map<String, Object> expense = (Map<String, Object>) expenses.get(expenseId);

                        String content = (String) expense.get("content");
                        String username = (String) expense.get("username");
                        double amount = ((java.lang.Number) expense.get("amount")).doubleValue();

                        expenseList.add(new Expense(content, username, amount));
                    }
                }
                if(members != null && members.size() > 0) {
                    for (String memberId : members.keySet()) {
                        String username = (String) members.get(memberId);
                        memberList.add(new User("", username));
                    }
                }

                if(transactions != null && transactions.size() > 0) {
                    for (String transactionId : transactions.keySet()) {

                        Map<String, Object> transaction = (Map<String, Object>) transactions.get(transactionId);

                        String content = (String) transaction.get("content");
                        String sendingUser = (String) transaction.get("sendingUser");
                        String receivingUser = (String) transaction.get("receivingUser");
                        double amount = ((java.lang.Number) transaction.get("amount")).doubleValue();

                        Transaction newTransaction = new Transaction();
                        newTransaction.setName(content);
                        newTransaction.setReceivingUsername(receivingUser);
                        newTransaction.setSendingUsername(sendingUser);
                        newTransaction.setAmount(amount);

                        transactionList.add(newTransaction);
                    }
                }

                UserSession.addGroup(new Group(groupName, groupId, memberList, expenseList, transactionList, true));

            }

            groupsListLoaded = true;
            databaseQueriedCallback();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    }

}

