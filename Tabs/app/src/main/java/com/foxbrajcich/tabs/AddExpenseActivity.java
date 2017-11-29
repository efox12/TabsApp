package com.foxbrajcich.tabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    Spinner spinner;
    List<User> users;
    ArrayAdapter<User> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        spinner = (Spinner) findViewById(R.id.spinner);
        Group group = (Group) getIntent().getSerializableExtra("group");
        users = group.getMembers();
        //group.getMembers();

        adapter = new ArrayAdapter<User>(this, android.R.layout.activity_list_item, android.R.id.text1, users) {
            @NonNull
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                ImageView textView1 = (ImageView) view.findViewById(android.R.id.icon);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text1);
                textView1.setImageResource(android.R.drawable.presence_online);
                textView2.setText(users.get(position).getName());
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView textView1 = (ImageView) view.findViewById(android.R.id.icon);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text1);
                textView1.setImageResource(android.R.drawable.presence_online);
                textView2.setText(users.get(position).getName());
                return view;
            }
        };
        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addExpense) {
            EditText expenseAmount = (EditText) findViewById(R.id.expenseAmount);
            EditText expenseDescription = (EditText) findViewById(R.id.expenseDiscription);
            if(expenseAmount.getText().length() > 0) {
                Intent intent = getIntent();
                Expense expense = new Expense();
                expense.setUserName(users.get(spinner.getSelectedItemPosition()).getName());
                expense.setAmount(Double.valueOf(expenseAmount.getText().toString()));
                expense.setContent(expenseDescription.getText().toString());
                intent.putExtra("expense", expense);
                setResult(RESULT_OK, intent);
                finishAfterTransition();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
                return true;
            }
        }
        if(item.getItemId() == android.R.id.home){
            Intent intent = getIntent();
            this.setResult(RESULT_OK, intent);
            finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        this.setResult(RESULT_OK, intent);
        finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
    }

}
