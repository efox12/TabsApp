package com.foxbrajcich.tabs;

import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class TransactionsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ActionBar actionBar = getSupportActionBar();
        if(getIntent().hasExtra("name")) {
            actionBar.setTitle(getIntent().getStringExtra("name"));
        }
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
    }

}
