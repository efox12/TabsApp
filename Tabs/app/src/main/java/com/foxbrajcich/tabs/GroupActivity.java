package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GroupActivity extends AppCompatActivity {
    Group group = new Group();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getIntent().getStringExtra("title"));
        if(getIntent().hasExtra("group")) {
            group = (Group) getIntent().getSerializableExtra("group");
            group.setGroupTitle(getIntent().getStringExtra("title"));
        }
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        Button button = (Button) findViewById(R.id.button2);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupActivity.this.finishAfterTransition();
                Intent intent = getIntent();
                String title = group.getGroupTitle();
                intent.putExtra("group", title);
                GroupActivity.this.setResult(Activity.RESULT_OK, intent);
                overridePendingTransition(R.anim.fade_in, R.anim.scale_down);

            }
        };
        button.setOnClickListener(listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            GroupActivity.this.finishAfterTransition();
            Intent intent = getIntent();
            intent.putExtra("group", group.getGroupTitle());
            this.setResult(Activity.RESULT_OK, intent);
            overridePendingTransition(R.anim.fade_in, R.anim.scale_down);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fade_in, R.anim.scale_down);
        super.onBackPressed();
    }

}
