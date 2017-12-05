package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    final int REQUEST_CODE = 1;
    int iconId;
    EditText title;
    Switch onlineSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Name your group");

        title = (EditText) findViewById(R.id.titleEditText);

        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        iconId = 0;
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


        ImageView imageView1 = (ImageView) findViewById(R.id.icon1);
        imageView1.setOnClickListener(this);

        ImageView imageView2 = (ImageView) findViewById(R.id.icon2);
        imageView2.setOnClickListener(this);

        ImageView imageView3 = (ImageView) findViewById(R.id.icon3);
        imageView3.setOnClickListener(this);

        ImageView imageView4 = (ImageView) findViewById(R.id.icon4);
        imageView4.setOnClickListener(this);

        ImageView imageView5 = (ImageView) findViewById(R.id.icon5);
        imageView5.setOnClickListener(this);

        ImageView imageView6 = (ImageView) findViewById(R.id.icon6);
        imageView6.setOnClickListener(this);

        ImageView imageView7 = (ImageView) findViewById(R.id.icon7);
        imageView7.setOnClickListener(this);

        ImageView imageView8 = (ImageView) findViewById(R.id.icon8);
        imageView8.setOnClickListener(this);

        ImageView imageView9 = (ImageView) findViewById(R.id.icon9);
        imageView9.setOnClickListener(this);

        ImageView imageView10 = (ImageView) findViewById(R.id.icon10);
        imageView10.setOnClickListener(this);

        onlineSwitch = findViewById(R.id.switch1);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.icon1){
            iconId = 1;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon1).setAlpha(1);
        }
        if(view.getId() == R.id.icon2){
            iconId = 2;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon2).setAlpha(1);
        }
        if(view.getId() == R.id.icon3){
            iconId =3;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon3).setAlpha(1);
        }
        if(view.getId() == R.id.icon4){
            iconId = 4;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon4).setAlpha(1);
        }
        if(view.getId() == R.id.icon5){
            iconId = 5;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon5).setAlpha(1);
        }
        if(view.getId() == R.id.icon6){
            iconId = 6;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon6).setAlpha(1);
        }
        if(view.getId() == R.id.icon7){
            iconId = 7;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon7).setAlpha(1);
        }
        if(view.getId() == R.id.icon8){
            iconId = 8;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon8).setAlpha(1);
        }
        if(view.getId() == R.id.icon9){
            iconId = 9;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon9).setAlpha(1);
        }
        if(view.getId() == R.id.icon10){
            iconId = 10;
            view.setAlpha(0.5f);
        } else {
            findViewById(R.id.icon10).setAlpha(1);
        }

        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(title.getRootView().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_title_menu, menu);
        final Menu menu1 = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.nextViewButton) {

            if(title.getText().length() < 1){
                title.setError("Please Enter a Title");
                return true;
            }
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(title.getWindowToken(), 0);
            Group group = new Group();
            group.setGroupIconId(iconId);
            group.setOnline(!onlineSwitch.isChecked());
            group.setGroupTitle(title.getText().toString());

            Intent intent = new Intent(this, AddFriendsToGroupActivity.class);
            intent.putExtra("group", group);
            startActivityForResult(intent, REQUEST_CODE);

            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            return true;
        }

        if(item.getItemId() == android.R.id.home){
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(title.getWindowToken(), 0);
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
        } else {
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
        super.onBackPressed();
    }

}

