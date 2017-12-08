package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsToGroupActivity extends AppCompatActivity {
    private ArrayAdapter<User> adapter;
    private List<User> users = new ArrayList<>();
    private List<User> groupMembers = new ArrayList<>();
    private List<String> groupMemberNames = new ArrayList<>();
    private Group group;
    private EditText editText;
    private User offlineUserEntry;
    private TextView addedUsersView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends_to_group);

        // set the action bar title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.addGroupMembers);

        // create the listView entry to add offline users
        offlineUserEntry = new User();
        offlineUserEntry.setUsername(getString(R.string.addOfflineUser));
        offlineUserEntry.setOnline(false);


        group = (Group) getIntent().getSerializableExtra(getString(R.string.group));

        ListView listView = (ListView) findViewById(R.id.friendsList);
        addedUsersView = findViewById(R.id.groupMembers);

        // adapter for displaying friends
        adapter = new ArrayAdapter<User>(this, R.layout.friend_list_layout, R.id.friendTextView, users){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                //get the views in the layout
                TextView textView1 = (TextView) view.findViewById(R.id.friendTextView);
                TextView textView2 = (TextView) view.findViewById(R.id.friendTextView2);

                if(users.get(position) == offlineUserEntry){
                    ((TextView) view.findViewById(R.id.friendshipStatus)).setVisibility(View.INVISIBLE);
                    ((ImageView) view.findViewById(R.id.friendImageView)).setImageResource(R.drawable.add);
                }else{
                    ((ImageView) view.findViewById(R.id.friendImageView)).setImageResource(R.drawable.user);
                }

                // set the views in the layout
                textView1.setText(users.get(position).getName());
                textView2.setText(users.get(position).getUsername());
                return view;
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        editText = (EditText) findViewById(R.id.addFriendsEditText);
        final TextView textView = (TextView) findViewById(R.id.groupMembers);
        // shows the keyboard when the activity starts
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
        // hides the keyboard when the user clicks outside the edit text
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        // hides keyboard when the user starts to scroll through the list
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        //
        User homeUser = new User(UserSession.getName(), UserSession.getUsername());
        groupMembers.add(homeUser);
        group.setMembers(groupMembers);
        addUsersNameToList(homeUser.getName() + getString(R.string.spaceMe));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User clickedUser = (User) adapterView.getItemAtPosition(i);
                if(clickedUser == offlineUserEntry){
                    // add an offline user to the group if the option is clicked
                    if(editText.length() > 0){
                        //create offline user
                        User user = new User();
                        user.setName(editText.getText().toString());
                        groupMembers.add(user);
                        addUsersNameToList(editText.getText().toString());
                        editText.setText(getString(R.string.empty));
                    }

                }else{
                    // add the corresponding online user to the group
                    groupMembers.add(clickedUser);
                    addUsersNameToList(clickedUser.getName() + getString(R.string.lparen) + clickedUser.getUsername() + getString(R.string.rparen));
                    editText.setText(getString(R.string.empty));
                }
            }
        });

        // display the add offline after text has been entered
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                users.clear();
                if(group.isOnline()){
                    users.addAll(filterFriends(s.toString()));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.length() > 0){
                    offlineUserEntry.setName(s.toString());
                    if(!users.contains(offlineUserEntry) && !userAlreadyAdded(offlineUserEntry)) {
                        users.add(0, offlineUserEntry);
                    }
                } else {
                    users.clear();
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_title_menu, menu);
        menu.getItem(0).setTitle(R.string.done);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(item.getItemId() == android.R.id.home){
            // hide the keyboard
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            Intent intent = getIntent();
            intent.putExtra("group", group);
            this.setResult(100, intent);

            // override the transition
            finishAfterTransition();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        if (id == R.id.nextViewButton) {
            // hide the keyboard
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            Intent intent = getIntent();
            intent.putExtra("group", group);
            this.setResult(RESULT_OK, intent);

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // override the transition
        finishAfterTransition();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }

    /**
     *
     * @param usersName
     */
    private void addUsersNameToList(String usersName){
        groupMemberNames.add(usersName);

        String newText = getString(R.string.members);

        for(int i = 0; i < groupMemberNames.size(); i++){
            if(i != 0) newText += getString(R.string.comma);
            newText += groupMemberNames.get(i);
        }

        addedUsersView.setText(newText);

    }

    /**
     *
     * @param filter
     * @return
     */
    private List<User> filterFriends(String filter){
        List<User> filteredUsers = new ArrayList<>();

        if(filter.length() < 1){
            return filteredUsers;
        }

        for(User user : UserSession.getFriends()){
            if(!userAlreadyAdded(user)) {
                if (user.getName().toLowerCase().startsWith(filter.toLowerCase()) ||
                        user.getUsername().toLowerCase().startsWith(filter.toLowerCase())) {
                    filteredUsers.add(user);
                }
            }
        }

        return filteredUsers;
    }

    /**
     *
     * @param u
     * @return
     */
    private boolean userAlreadyAdded(User u){
        for(User user : groupMembers){
            if(user.getName().toLowerCase().equals(u.getName().toLowerCase())){
                return true;
            }

            if(!user.getUsername().equals(getString(R.string.empty)) && user.getUsername().toLowerCase().equals(u.getUsername().toLowerCase())){
                return true;
            }
        }

        return false;
    }
}