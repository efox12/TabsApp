package com.foxbrajcich.tabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class AddExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addExpense) {
            Intent intent = getIntent();
            Expense expense = new Expense();
            EditText expenseAmount = (EditText) findViewById(R.id.expenseAmount);
            EditText expenseDescription = (EditText) findViewById(R.id.expenseDiscription);
            expense.setAmount(Double.valueOf(expenseAmount.getText().toString()));
            expense.setContent(expenseDescription.getText().toString());
            intent.putExtra("expense", expense);
            setResult(RESULT_OK, intent);
            finishAfterTransition();
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            Intent intent = getIntent();
            this.setResult(RESULT_OK, intent);
            finishAfterTransition();
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
