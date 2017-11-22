package com.foxbrajcich.tabs;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class GroupActivity extends AppCompatActivity {
    Group group = new Group();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("title"));
        group = (Group) getIntent().getSerializableExtra("group");
        group.setGroupTitle(getIntent().getStringExtra("title"));
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finishAfterTransition();
            Intent intent = getIntent();
            intent.putExtra("group", group);
            setResult(RESULT_OK, intent);
            overridePendingTransition(R.anim.fade_in, R.anim.scale_down);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.scale_down);
    }
}
