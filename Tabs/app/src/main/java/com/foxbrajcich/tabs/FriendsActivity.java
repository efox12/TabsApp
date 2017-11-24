package com.foxbrajcich.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class FriendsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ActionBar actionBar = getSupportActionBar();
        if(getIntent().hasExtra("name")) {
            actionBar.setTitle(getIntent().getStringExtra("name"));
        }
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
    }
}
