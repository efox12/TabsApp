package com.foxbrajcich.tabs;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    LocalDatabaseHelper dbHelper;
    List<Group> groupList;
    ArrayAdapter<Group> groupsAdapter;

    final static int NEW_REQUEST_CODE = 1;
    final static int EXISTING_GROUP_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //populate the list of groups from the local database
        dbHelper = LocalDatabaseHelper.getInstance(this);
        groupList = dbHelper.getAllOfflineGroups();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateGroupActivity.class);
                startActivityForResult(intent, NEW_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_up_bottom, R.anim.fade_out);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.drawerNameTextView)).setText(UserSession.getName());
        ((TextView) headerView.findViewById(R.id.drawerUsernameTextView)).setText(UserSession.getUsername());
        navigationView.setNavigationItemSelectedListener(this);

        ListView listView =  (ListView) findViewById(R.id.groupList);
        if(groupList.size() == 0){
            TextView groupEmptyText = (TextView) findViewById(R.id.noFriendsText);
            groupEmptyText.setText("No Groups Created");
            findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
            findViewById(R.id.noFriendsSadFace).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.noFriendsText).setVisibility(View.GONE);
            findViewById(R.id.noFriendsSadFace).setVisibility(View.GONE);
        }

        groupsAdapter = new ArrayAdapter<Group>(this, R.layout.group_list_layout, R.id.groupTextView, groupList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                TextView textView = (TextView) view.findViewById(R.id.groupTextView);
                TextView textView1 = (TextView) view.findViewById(R.id.groupTextView2);
                textView.setText(groupList.get(position).getGroupTitle());
                textView1.setText("Group");
                imageView.setImageResource(getImage(groupList.get(position).getGroupIconId()));
                return view;
            }
        };

        listView.setAdapter(groupsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                intent.putExtra("group", groupList.get(i));
                intent.putExtra("position", i);
                startActivityForResult(intent, EXISTING_GROUP_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivityForResult(intent, NEW_REQUEST_CODE);
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
        } else if (id == R.id.findFriends) {
            Intent intent = new Intent(MainActivity.this, FindFriendsActivity.class);
            startActivityForResult(intent, NEW_REQUEST_CODE);
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
        } else if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, NEW_REQUEST_CODE);
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
        } else if (id == R.id.help) {

        } else if (id == R.id.signOut) {
            SharedPreferences preferences = this.getSharedPreferences(LoginActivity.PREFERENCES_FILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("name", "");
            editor.putString("username", "");
            editor.commit();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(data.hasExtra("group")) {
                Group group = (Group) data.getSerializableExtra("group");
                if(!group.isOnline()) dbHelper.addGroupToDatabase(group); //if it's an offline group add it to the local db
                groupList.add(group);

                //open newly added group in GroupActivity
                Intent intent = new Intent(this, GroupActivity.class);
                intent.putExtra("group", group);
                intent.putExtra("position", groupList.size() - 1);
                startActivityForResult(intent, EXISTING_GROUP_REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }
        if (requestCode == EXISTING_GROUP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(data.hasExtra("group")) {
                Group group = (Group) data.getSerializableExtra("group");
                if(data.hasExtra("position")){
                    int position = data.getIntExtra("position", 0);
                    groupList.set(position, group);
                }
            }
        }
    }

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

}