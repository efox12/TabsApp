package com.foxbrajcich.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindFriendsActivity extends AppCompatActivity {

    private EditText mSearchView;
    private ListView mListView;
    private ArrayAdapter<User> arrayAdapter;
    private List<User> filteredUsers;

    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        mSearchView = (EditText) findViewById(R.id.findFriendsSearch);

        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        mListView = (ListView) findViewById(R.id.findFriendsListView);

        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        filteredUsers = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_2, android.R.id.text1, filteredUsers){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                User user = getItem(position);

                ((TextView) v.findViewById(android.R.id.text1)).setText(user.getName());
                ((TextView) v.findViewById(android.R.id.text2)).setText(user.getUsername());

                return v;
            }
        };

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final User user = (User) adapterView.getItemAtPosition(i);
                new AlertDialog.Builder(FindFriendsActivity.this).setTitle("Add Friend")
                        .setMessage("Would you like to add " + user.getName() + " (" + user.getUsername() + ") to your friends list?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UserSession.addFriend(new User(user.getName(), user.getUsername()));
                                filteredUsers.remove(user);
                                mFirebaseDatabase.getReference("users").child(UserSession.getUsername()).child("friends").push().setValue(user.getUsername());
                                mSearchView.setText("");
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        mListView.setAdapter(arrayAdapter);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSearchView.addTextChangedListener(new SearchTypedHandler());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right);
        super.onBackPressed();
    }

    //handles events when user types in the edit text
    private class SearchTypedHandler implements TextWatcher {

        private DatabaseReference mDataRef;
        private QueryHandler currentHandler;
        private Query lastQuery;

        SearchTypedHandler(){
            mDataRef = mFirebaseDatabase.getReference("users");
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String s = charSequence.toString().toLowerCase();

            //get rid of the last handler to allow the new one to take over
            if(lastQuery != null){
                lastQuery.removeEventListener(currentHandler);
            }

            filteredUsers.clear(); //empty the list in preparation to populate it
            arrayAdapter.notifyDataSetChanged();
            if(!s.equals("")){
                lastQuery = mDataRef.orderByKey().startAt(s).endAt(s+"\uf8ff");
                currentHandler = new QueryHandler();
                lastQuery.addValueEventListener(currentHandler);
            }else{
                lastQuery = null;
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //do nothing
        }

    }

    //handles results back from the firebase query
    private class QueryHandler implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(!dataSnapshot.exists()){
                return;
            }

            Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();

            for(String username : users.keySet()){
                if(shouldShowUser(username)) {
                    Map<String, Object> userData = (Map<String, Object>) users.get(username);
                    filteredUsers.add(new User((String) userData.get("name"), username));
                }
            }

            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            //do nothing
        }
    }

    private boolean shouldShowUser(String username){
        if(UserSession.getUsername().equals(username)){
            return false;
        }

        for(User u : UserSession.getFriends()){
            if(u.getUsername().equals(username)){
                return false;
            }
        }

        return true;
    }

}
