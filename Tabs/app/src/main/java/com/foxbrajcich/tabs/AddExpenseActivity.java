package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    Spinner spinner;
    List<User> users;
    ArrayAdapter<User> adapter;
    EditText expenseAmount;
    EditText expenseDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        spinner = (Spinner) findViewById(R.id.spinner);
        final Group group = (Group) getIntent().getSerializableExtra("group");

        users = new ArrayList<>();

        for(User u : group.getMembers()){
            if(u.getUsername().length() < 1){
                users.add(u);
            }else if(u.getUsername().equals(UserSession.getUsername())){
                users.add(0, u);
            }
        }

        adapter = new ArrayAdapter<User>(this, android.R.layout.activity_list_item, android.R.id.text1, users) {
            @NonNull
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                ImageView textView1 = (ImageView) view.findViewById(android.R.id.icon);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text1);
                textView1.setImageResource(R.drawable.user);

                String username = users.get(position).getUsername();
                String name = users.get(position).getName();

                String suffix = "";

                if(username.equals(UserSession.getUsername())){
                    suffix = " (me)";
                }else if(username.length() > 0){
                    suffix = " (" + username + ")";
                }

                if(!group.isOnline() && name.equals(UserSession.getName())){
                    suffix = " (me)";
                }

                textView2.setText(name + suffix);
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView textView1 = (ImageView) view.findViewById(android.R.id.icon);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text1);
                textView1.setImageResource(R.drawable.user);

                String username = users.get(position).getUsername();
                String name = users.get(position).getName();

                String suffix = "";

                if(username.equals(UserSession.getUsername())){
                    suffix = " (me)";
                }else if(username.length() > 0){
                    suffix = " (" + username + ")";
                }

                if(!group.isOnline() && name.equals(UserSession.getName())){
                    suffix = " (me)";
                }

                textView2.setText(name + suffix);
                return view;
            }
        };
        spinner.setAdapter(adapter);

        expenseAmount = (EditText) findViewById(R.id.expenseAmount);
        expenseDescription = (EditText) findViewById(R.id.expenseDiscription);

        expenseAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        expenseDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        expenseAmount.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expense_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addExpense) {

            if(expenseAmount.getText().length() > 0) {
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(expenseAmount.getWindowToken(), 0);
                InputMethodManager inputMethodManager2 =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager2.hideSoftInputFromWindow(expenseDescription.getWindowToken(), 0);

                Expense expense = new Expense();
                expense.setUsersName(users.get(spinner.getSelectedItemPosition()).getName());
                expense.setUsername(users.get(spinner.getSelectedItemPosition()).getUsername());
                expense.setAmount(Double.valueOf(expenseAmount.getText().toString()));
                expense.setContent(expenseDescription.getText().toString());

                Intent intent = getIntent();
                intent.putExtra("expense", expense);
                setResult(RESULT_OK, intent);
                finishAfterTransition();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_down_top);
                return true;
            }
        }
        if(item.getItemId() == android.R.id.home){
            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(expenseAmount.getWindowToken(), 0);
            InputMethodManager inputMethodManager2 =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager2.hideSoftInputFromInputMethod(expenseDescription.getWindowToken(), 0);
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
