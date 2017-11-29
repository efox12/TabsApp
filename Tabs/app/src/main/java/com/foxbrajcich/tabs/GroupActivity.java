package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    final static int REQUEST_CODE = 1;
    Group group;
    List<Expense> expenses = new ArrayList<>();
    ArrayAdapter<Expense> adapter;
    LocalDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        //get instance of the offline group database
        dbHelper = LocalDatabaseHelper.getInstance(this);

        ActionBar actionBar = getSupportActionBar();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, AddExpenseActivity.class);
                intent.putExtra("group", group);
                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition(R.anim.slide_up_bottom, R.anim.fade_out);
            }
        });

        if(getIntent().hasExtra("group")) {
            group = (Group) getIntent().getSerializableExtra("group");
            actionBar.setTitle(group.getGroupTitle());
            expenses = group.getExpenses();
        }

        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
        ListView listView = (ListView) findViewById(R.id.expenseList);
        adapter = new ArrayAdapter<Expense>(this, R.layout.expense_list_layout, R.id.nameTextView, expenses){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                TextView textView = (TextView) view.findViewById(R.id.nameTextView);
                TextView textView2 = (TextView) view.findViewById(R.id.amountTextView);
                TextView textView3 = (TextView) view.findViewById(R.id.contentTextView);
                imageView.setImageResource(android.R.drawable.btn_plus);
                textView.setText(expenses.get(position).getUserName());
                textView2.setText("$"+String.format("%.02f",expenses.get(position).getAmount()));
                textView3.setText(expenses.get(position).getContent());
                return view;
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for(User user : group.getMembers()){
            for(Debt d : group.getDebtsForUser(user)) {
                Log.d("TEST_FUNCTION", user.getName() + ", debt: " + d);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addExpense) {
            Intent intent = new Intent(GroupActivity.this, GroupInfoActivity.class);
            intent.putExtra("group", group);
            startActivityForResult(intent, REQUEST_CODE);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
          return true;
        }
        if(item.getItemId() == android.R.id.home){
            Intent intent = getIntent();
            intent.putExtra("group", group);
            this.setResult(RESULT_OK, intent);
            GroupActivity.this.finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.scale_down);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("group", group);
        this.setResult(RESULT_OK, intent);
        GroupActivity.this.finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.scale_down);
        //super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(data.hasExtra("expense")){
                expenses.add((Expense) data.getSerializableExtra("expense"));
                dbHelper.addExpenseToGroup(group,(Expense) data.getSerializableExtra("expense"));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
