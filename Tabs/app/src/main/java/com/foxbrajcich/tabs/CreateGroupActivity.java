package com.foxbrajcich.tabs;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class CreateGroupActivity extends AppCompatActivity {
    final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Name your group");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_title_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        EditText title = (EditText) findViewById(R.id.titleEditText);
        //noinspection SimplifiableIfStatement
        if (id == R.id.nextViewButton) {
            Intent intent = new Intent(CreateGroupActivity.this, AddFriendsToGroupActivity.class);
            intent.putExtra("title", title.getText().toString());
            startActivityForResult(intent, REQUEST_CODE);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            return true;
        }

        if(item.getItemId() == android.R.id.home){
            finishAfterTransition();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        intent.putExtra("group", data.getSerializableExtra("group"));
        super.onActivityResult(requestCode, resultCode, data);
    }
}
