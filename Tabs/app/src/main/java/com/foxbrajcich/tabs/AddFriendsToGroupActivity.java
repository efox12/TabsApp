package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AddFriendsToGroupActivity extends AppCompatActivity {
    ArrayAdapter<User> adapter;
    List<User> users = new ArrayList<>();
    List<User> groupMembers = new ArrayList<>();
    List<String> groupMemberNames = new ArrayList<>();
    Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends_to_group);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add friends");
        User user = new User();
        user.setName("Add Offline User");
        group = (Group) getIntent().getSerializableExtra("group");

        users.add(0,user);
        ListView listView = (ListView) findViewById(R.id.friendsList);
        adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_2, android.R.id.text1, users){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
                textView1.setText(users.get(position).getName());
                textView2.setText("Friend");
                return view;
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        final EditText editText = (EditText) findViewById(R.id.addFriendsEditText);
        final TextView textView = (TextView) findViewById(R.id.groupMembers);
        textView.setText("Members: ");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(editText.length() > 0){
                    User user = new User();
                    user.setName(editText.getText().toString());
                    groupMembers.add(user);
                    group.setMembers(groupMembers);
                    groupMemberNames.add(editText.getText().toString());
                    editText.setText("");
                    textView.setText("Members: " + groupMemberNames.toString());
                }
            }
        });
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
        if(item.getItemId() == android.R.id.home){
            Intent intent = getIntent();
            intent.putExtra("group", group);
            this.setResult(100, intent);
            finishAfterTransition();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.nextViewButton) {
            Intent intent = getIntent();
            intent.putExtra("group", group);
            this.setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }
}
