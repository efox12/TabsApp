package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {

    final static int REQUEST_ADD_EXPENSE = 1;
    final static int REQUEST_VIEW_INFO = 2;

    private Group group;
    private List<Expense> expenses = new ArrayList<>();
    private ArrayAdapter<Expense> adapter;
    private LocalDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        //get instance of the offline group database
        dbHelper = LocalDatabaseHelper.getInstance(this);

        ActionBar actionBar = getSupportActionBar();
        // add an expense when fab is clicked
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, AddExpenseActivity.class);
                intent.putExtra("group", group);

                // override transition
                startActivityForResult(intent, REQUEST_ADD_EXPENSE);
                overridePendingTransition(R.anim.slide_up_bottom, R.anim.fade_out);
            }
        });

        // set the action bar title and get extras
        if(getIntent().hasExtra("group")) {
            group = (Group) getIntent().getSerializableExtra("group");
            actionBar.setTitle(group.getGroupTitle());
            expenses = group.getExpenses();
        }
        // override the up button
        actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);

        // the adapter for displaying expenses
        ListView listView = (ListView) findViewById(R.id.expenseList);
        adapter = new ArrayAdapter<Expense>(this, R.layout.expense_list_layout, R.id.nameTextView, expenses){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // get the views in the list layout
                ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                TextView textView = (TextView) view.findViewById(R.id.nameTextView);
                TextView textView2 = (TextView) view.findViewById(R.id.amountTextView);
                TextView textView3 = (TextView) view.findViewById(R.id.contentTextView);

                // set the views in the list layout
                imageView.setImageResource(R.drawable.user);
                textView.setText(expenses.get(position).getUsersName());
                textView2.setText(getString(R.string.$)+String.format("%.02f",expenses.get(position).getAmount()));
                textView3.setText(expenses.get(position).getContent());
                return view;
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.group_info) {
            //view info on the group
            Intent intent = new Intent(GroupActivity.this, GroupInfoActivity.class);
            intent.putExtra("group", group);

            // override the transition
            startActivityForResult(intent, REQUEST_VIEW_INFO);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
          return true;
        }
        if(item.getItemId() == android.R.id.home){
            //go back to the main screen
            Intent intent = getIntent();
            intent.putExtra("group", group);
            this.setResult(RESULT_OK, intent);

            //override the transition
            GroupActivity.this.finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.scale_down);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // go back to the main view
        Intent intent = getIntent();
        intent.putExtra("group", group);
        this.setResult(RESULT_OK, intent);

        // override the transition
        GroupActivity.this.finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.scale_down);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_EXPENSE && resultCode == Activity.RESULT_OK) {
            // if coming from adding an expense
            if(data.hasExtra("expense")){
                // add the expense to the list of expenses and display it
                Expense expense = (Expense) data.getSerializableExtra("expense");
                expenses.add(0, expense);
                adapter.notifyDataSetChanged();
                // add the group to the respective database
                if(!group.isOnline()) dbHelper.addExpenseToGroup(group,(Expense) data.getSerializableExtra("expense"));
                else addExpenseToFirebaseGroup(expense, group);
            }
        } else if (requestCode == REQUEST_VIEW_INFO && resultCode == Activity.RESULT_OK) {
            // if coming from group info
            if(data.hasExtra("group")){
                // update the group
                Group updatedGroup = (Group) data.getSerializableExtra("group");
                // add the transactions to the group
                group.setTransactions(updatedGroup.getTransactions());
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     *
     * @param expense
     * @param group
     */
    private void addExpenseToFirebaseGroup(Expense expense, Group group){
        DatabaseReference expensesReference = FirebaseDatabase.getInstance().getReference("groups").child(group.getGroupId()).child("expenses");

        Map<String, Object> expenseInfo = new HashMap<>();
        expenseInfo.put("content", expense.getContent());
        expenseInfo.put("amount", expense.getAmount());
        expenseInfo.put("usersName", expense.getUsersName());
        expenseInfo.put("username", expense.getUsername());

        expensesReference.push().setValue(expenseInfo);
    }

}
