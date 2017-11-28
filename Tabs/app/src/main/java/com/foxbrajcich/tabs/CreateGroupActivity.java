package com.foxbrajcich.tabs;

import android.app.Activity;
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
            Group group = new Group();
            group.setOnline(false);
            if(title.getText().length() > 0) {
                group.setGroupTitle(title.getText().toString());
            } else {
                group.setGroupTitle("NO TITLE SET");
            }
            Intent intent = new Intent(this, AddFriendsToGroupActivity.class);
            intent.putExtra("group", group);
            startActivityForResult(intent, REQUEST_CODE);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            return true;
        }

        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent();
            this.setResult(Activity.RESULT_OK, intent);
            this.finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            this.setResult(Activity.RESULT_OK, data);
            this.finish();
        }
        //if (requestCode == REQUEST_CODE && resultCode == 100) {

        //}
    }

    @Override
    public void onBackPressed() {
        this.finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
        super.onBackPressed();
    }

}

