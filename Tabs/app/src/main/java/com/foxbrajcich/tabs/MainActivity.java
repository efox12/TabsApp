package com.foxbrajcich.tabs;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainActivityFragment.OnSectionSelected,
        MainActivityFragment.OnGetList {

    LocalDatabaseHelper dbHelper;

    List<Group> groupList;
    List<User> friendList = new ArrayList<>();
    List<Transaction> transactionList = new ArrayList<>();

    final static int REQUEST_CODE = 1;
    final static int NEW_REQUEST_CODE = 2;
    final static int EXISTING_GROUP_REQUEST_CODE = 3;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //populate the list of groups from the local database
        dbHelper = LocalDatabaseHelper.getInstance(this);
        groupList = dbHelper.getAllOfflineGroups();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //mSectionsPagerAdapter.setUpFragments();
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
        navigationView.setNavigationItemSelectedListener(this);
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateFragment(int i){
        List<Fragment> allFragments = getSupportFragmentManager().getFragments();
        if (allFragments != null) {
            for (Fragment fragment : allFragments) {
                MainActivityFragment f1 = (MainActivityFragment) fragment;
                if (f1.fragmentNumber == i)
                    f1.updateGroups();
            }
        }
    }

    @Override
    public List<Group> getGroupList(){
        return groupList;
    }

    @Override
    public List<User> getFriendList(){
        return friendList;
    }

    @Override
    public List<Transaction> getTransactionList(){
        return transactionList;
    }

    @Override
    public void onFriendSelected(int position) {
        Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
        intent.putExtra("friend", friendList.get(position).toString());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onGroupSelected(int position) {
        Intent intent = new Intent(MainActivity.this, GroupActivity.class);
        intent.putExtra("group", groupList.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, EXISTING_GROUP_REQUEST_CODE);
    }

    @Override
    public void onTransactionSelected(int position) {
        Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
        intent.putExtra("transaction", transactionList.get(position));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(data.hasExtra("group")) {
                Group group = (Group) data.getSerializableExtra("group");
                if(!group.isOnline()) dbHelper.addGroupToDatabase(group); //if it's an offline group add it to the local db
                groupList.add(group);
                updateFragment(1);

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
                    updateFragment(1);
                }
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            MainActivityFragment f = MainActivityFragment.newInstance(position + 1);
            f.fragmentNumber = position;
            return f;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Friends";
                case 1:
                    return "Groups";
                case 2:
                    return "Transactions";
            }
            return null;
        }
    }
}