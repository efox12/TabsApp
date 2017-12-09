package com.foxbrajcich.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.foxbrajcich.tabs.R.drawable.user;

public class FriendsActivity extends AppCompatActivity {

    private User user;
    private List<Group> groups = new ArrayList<>();
    private ArrayAdapter<Group> adapter;
    private boolean addedFriend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // hide the keyboard if it is displayed
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        user = (User) getIntent().getSerializableExtra("user");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.userInfo);
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        ((TextView) findViewById(R.id.textView)).setText(user.getName());
        ((TextView) findViewById(R.id.textView4)).setText(user.getUsername());
        final TextView textView = (TextView) findViewById(R.id.editProfileButton);
        if(isFriend(user)){
            textView.setText(getString(R.string.friends));
        } else{
            textView.setText(getString(R.string.addFriend));
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFriend(user)) {
                    new AlertDialog.Builder(FriendsActivity.this).setTitle(R.string.addFriend)
                            .setMessage(getString(R.string.wouldYouLikeToAdd) + user.getName() + getString(R.string.lparen) + user.getUsername() + getString(R.string.toYourFriendsList))
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    textView.setText(getString(R.string.friends));
                                    UserSession.addFriend(new User(user.getName(), user.getUsername()));
                                    addedFriend = true;
                                }
                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                }
            }
        });
        groups.clear();
        for(int i = 0; i < UserSession.getGroupsList().size(); i++){
            for(int j = 0; j < UserSession.getGroupsList().get(i).getMembers().size(); j++)
            if(UserSession.getGroupsList().get(i).getMembers().get(j).getUsername().equals(user.getUsername())){
                groups.add(UserSession.getGroupsList().get(i));
            }
        }

        ListView listView = (ListView) findViewById(R.id.activityList);
        adapter = new ArrayAdapter<Group>(this, R.layout.group_list_layout, R.id.groupTextView, groups){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // get views in list layout
                ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                TextView textView = (TextView) view.findViewById(R.id.groupTextView);
                TextView textView2 = (TextView) view.findViewById(R.id.groupTextView2);
                imageView.setImageResource(R.drawable.user);
                // set views in list layout
                imageView.setImageResource(getImage(groups.get(position).getGroupIconId()));
                textView.setText(groups.get(position).getGroupTitle());
                textView2.setText(getString(R.string.onlineGroup));
                return view;
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getListHeight(listView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = getIntent();
            intent.putExtra("user",user);
            if(addedFriend){
                setResult(RESULT_OK, intent);
            }
            finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("user",user);
        if(addedFriend){
            setResult(RESULT_OK, intent);
        }
        finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
        super.onBackPressed();
    }

    /**
     * sets the height of the listview to the expanded list height
     * @param listView the listview
     */
    public static void getListHeight (ListView listView) {

        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        // add the height of each individual list item
        ViewGroup viewGroup = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, viewGroup);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // set the height of the whole list
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    /**
     * gets the drawable integer corresponding to an image index
     * @param i the image index
     * @return the integer representing the image
     */
    public int getImage(int i) {
        if (i == 1) {
            return R.drawable.basketball;
        } else if (i == 2) {
            return R.drawable.bee;
        } else if (i == 3) {
            return R.drawable.poo;
        } else if (i == 4) {
            return R.drawable.diamond;
        } else if (i == 5) {
            return R.drawable.game;
        } else if (i == 6) {
            return R.drawable.map;
        } else if (i == 7) {
            return R.drawable.gift;
        } else if (i == 8) {
            return R.drawable.house;
        } else if (i == 9) {
            return R.drawable.camping;
        } else if (i == 10) {
            return R.drawable.monster;
        } else {
            return R.drawable.user;
        }
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
