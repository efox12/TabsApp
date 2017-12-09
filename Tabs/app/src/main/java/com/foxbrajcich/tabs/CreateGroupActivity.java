/* Sources
/* All icons can be credited to
/* Freepik and Smashicons from www.flaticon.com
*/

package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private final int REQUEST_CODE = 1;
    private int iconId;
    private EditText title;
    private Switch onlineSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Name your group");

        // open keyboard to edit text when activity is started
        title = (EditText) findViewById(R.id.titleEditText);
        title.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        // hide keyboard when click outside of editText
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        iconId = 0;
        // get the group icons
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

        // add a switch to choose online or offline group
        onlineSwitch = findViewById(R.id.switch1);
        onlineSwitch.setChecked(true);
        onlineSwitch.setText(R.string.onlineGroup);

        // change text to match selected group type
        onlineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    onlineSwitch.setText(R.string.onlineGroup);
                } else {
                    onlineSwitch.setText(R.string.offlineGroup);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        // set the group icon to the selected icon
        if(view.getId() == R.id.icon1){
            if(iconId == 1){
                iconId = 0;
                findViewById(R.id.icon1).setAlpha(1);
            } else {
                iconId = 1;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon1).setAlpha(1);
        }
        if(view.getId() == R.id.icon2){
            if(iconId == 2){
                iconId = 0;
                findViewById(R.id.icon2).setAlpha(1);
            } else {
                iconId = 2;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon2).setAlpha(1);
        }
        if(view.getId() == R.id.icon3){
            if(iconId == 3){
                iconId = 0;
                findViewById(R.id.icon3).setAlpha(1);
            } else {
                iconId = 3;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon3).setAlpha(1);
        }
        if(view.getId() == R.id.icon4){
            if(iconId == 4){
                iconId = 0;
                findViewById(R.id.icon4).setAlpha(1);
            } else {
                iconId = 4;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon4).setAlpha(1);
        }
        if(view.getId() == R.id.icon5){
            if(iconId == 5) {
                iconId = 0;
                findViewById(R.id.icon5).setAlpha(1);
            } else {
                iconId = 5;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon5).setAlpha(1);
        }
        if(view.getId() == R.id.icon6){
            if(iconId == 6){
                iconId = 0;
                findViewById(R.id.icon6).setAlpha(1);
            } else {
                iconId = 6;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon6).setAlpha(1);
        }
        if(view.getId() == R.id.icon7){
            if(iconId == 7){
                iconId = 0;
                findViewById(R.id.icon7).setAlpha(1);
            } else {
                iconId = 7;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon7).setAlpha(1);
        }
        if(view.getId() == R.id.icon8){
            if(iconId == 8){
                iconId = 0;
                findViewById(R.id.icon8).setAlpha(1);
            } else {
                iconId = 8;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon8).setAlpha(1);
        }
        if(view.getId() == R.id.icon9 ){
            if(iconId == 9){
                iconId = 0;
                findViewById(R.id.icon9).setAlpha(1);
            } else {
                iconId = 9;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon9).setAlpha(1);
        }
        if(view.getId() == R.id.icon10){
            if(iconId == 10){
                iconId = 0;
                findViewById(R.id.icon10).setAlpha(1);
            } else {
                iconId = 10;
                view.setAlpha(0.5f);
            }
        } else {
            findViewById(R.id.icon10).setAlpha(1);
        }
        // close the keyboard
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(title.getRootView().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_title_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nextViewButton) {

            if(title.getText().length() < 1){
                title.setError(getString(R.string.pleaseEnterATitle));
                return true;
            }
            // close the keyboard
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(title.getWindowToken(), 0);
            // create a new group and set the relevant fields
            Group group = new Group();
            group.setGroupIconId(iconId);
            group.setOnline(onlineSwitch.isChecked());
            group.setGroupTitle(title.getText().toString());

            Intent intent = new Intent(this, AddFriendsToGroupActivity.class);
            intent.putExtra("group", group);

            // override the transition
            startActivityForResult(intent, REQUEST_CODE);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            return true;
        }

        if(item.getItemId() == android.R.id.home){
            // close the keyboard
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(title.getWindowToken(), 0);

            Intent intent = new Intent();
            this.setResult(Activity.RESULT_OK, intent);

            // override the transition
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
            // hide the keyboard
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @Override
    public void onBackPressed() {
        // override the transition
        this.finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
        super.onBackPressed();
    }
}

