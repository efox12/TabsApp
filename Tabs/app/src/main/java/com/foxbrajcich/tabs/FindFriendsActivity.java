package com.foxbrajcich.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    final int REQUEST_CODE = 1;
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

        // open the keyboard to the edit text when the activity starts
        mSearchView.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        // hide the keyboard when click outside the edit text
        mSearchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        // array adapter for the list of online users
        mListView = (ListView) findViewById(R.id.findFriendsListView);
        filteredUsers = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<User>(this, R.layout.friend_list_layout, R.id.friendTextView, filteredUsers){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                User user = getItem(position);

                // set the values of the views
                ((ImageView) v.findViewById(R.id.friendImageView)).setImageResource(R.drawable.user);
                ((TextView) v.findViewById(R.id.friendTextView)).setText(user.getName());
                ((TextView) v.findViewById(R.id.friendTextView2)).setText(user.getUsername());

                // display this view if the user is a friend
                if(isFriend(user)){
                    ((TextView) v.findViewById(R.id.friendshipStatus)).setVisibility(View.VISIBLE);
                }else{
                    ((TextView) v.findViewById(R.id.friendshipStatus)).setVisibility(View.INVISIBLE);
                }

                return v;
            }
        };
        mListView.setAdapter(arrayAdapter);

        // show option to add user to friends list if they are clicked
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final User user = (User) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(FindFriendsActivity.this, FriendsActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, REQUEST_CODE);
                /*if(!isFriend(user)) {
                    new AlertDialog.Builder(FindFriendsActivity.this).setTitle(R.string.addFriend)
                            .setMessage(getString(R.string.wouldYouLikeToAdd) + user.getName() + getString(R.string.lparen) + user.getUsername() + getString(R.string.toYourFriendsList))
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UserSession.addFriend(new User(user.getName(), user.getUsername()));
                                    filteredUsers.remove(user);
                                    mFirebaseDatabase.getReference(getString(R.string.users)).child(UserSession.getUsername()).child(getString(R.string.friends)).push().setValue(user.getUsername());
                                    mSearchView.setText(getString(R.string.empty));
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                }*/
            }
        });

        filteredUsers.addAll(UserSession.getFriends());
        arrayAdapter.notifyDataSetChanged();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSearchView.addTextChangedListener(new SearchTypedHandler());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            // close the keyboard
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

            // override the transition
            finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // override the transition
        finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right);
        super.onBackPressed();
    }

    //handles events when user types in the edit text
    private class SearchTypedHandler implements TextWatcher {

        private DatabaseReference mDataRef;
        private QueryHandler currentHandler;
        private Query lastQuery;

        /**
         *
         */
        SearchTypedHandler(){
            mDataRef = mFirebaseDatabase.getReference("users");
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            //get rid of the last handler to allow the new one to take over
            if (lastQuery != null) {
                lastQuery.removeEventListener(currentHandler);
            }

            if(charSequence.length() < 1){
                filteredUsers.clear();
                filteredUsers.addAll(UserSession.getFriends());
                arrayAdapter.notifyDataSetChanged();
                lastQuery = null;
            }else{
                String s = charSequence.toString().toLowerCase();

                filteredUsers.clear(); //empty the list in preparation to populate it
                arrayAdapter.notifyDataSetChanged();

                lastQuery = mDataRef.orderByKey().startAt(s).endAt(s + "\uf8ff");
                currentHandler = new QueryHandler();
                lastQuery.addValueEventListener(currentHandler);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_OK){
            User user = (User) getIntent().getSerializableExtra("user");
            UserSession.addFriend(new User(user.getName(), user.getUsername()));
            filteredUsers.remove(user);
            mFirebaseDatabase.getReference(getString(R.string.users)).child(UserSession.getUsername()).child(getString(R.string.friends)).push().setValue(user.getUsername());
            mSearchView.setText(getString(R.string.empty));
            arrayAdapter.notifyDataSetChanged();
        }
    }

    /**
     *
     * @param username
     * @return
     */
    private boolean shouldShowUser(String username){
        if(UserSession.getUsername().equals(username)){
            return false;
        }

        return true;
    }

    /**
     *
     * @param username
     * @param name
     * @param user
     * @return
     */
    private boolean isSameUser(String username, String name, User user){

        if(username.equals(user.getUsername())){
            if(username.length() < 1){ //if both users are offline
                return name.equals(user.getName());
            }else{
                return true;
            }
        }

        return false;

    }

    /**
     *
     * @param user
     * @return
     */
    private boolean isFriend(User user){
        for(User friend: UserSession.getFriends()){
            if(isSameUser(user.getUsername(), user.getName(), friend)){
                return true;
            }
        }

        return false;
    }

}
