package com.foxbrajcich.tabs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GroupInfoActivity extends AppCompatActivity {

    ArrayAdapter<Debt> adapter;
    List<User> groupMembers = new ArrayList<>();
    Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        group = (Group) getIntent().getSerializableExtra("group");
        groupMembers = group.getMembers();
        TextView totalGroupExpenseView =(TextView) findViewById(R.id.textView4);
        TextView totalUserExpenseView = (TextView) findViewById(R.id.textView5);
        totalGroupExpenseView.setText("Group Total: $" + String.format("%.02f",totalExpenses(group.getExpenses())));
        ListView listView = (ListView) findViewById(R.id.friendsList);

        //find the object for the user who is logged in
        User localUser = null;
        for(User user : groupMembers) {
            if(user.getName().equals(UserSession.getName())){
                localUser = user;
                break;
            }
        }

        //get the list of debts that this user owes
        final List<Debt> debts;
        if(localUser != null) {
            debts = group.getDebtsForUser(localUser);
            totalUserExpenseView.setText("Your Expenses: $" + String.format("%.02f", group.getTotalExpenseForUser(localUser)));
        }else{
            debts = new ArrayList<>();
        }

        adapter = new ArrayAdapter<Debt>(this, R.layout.expense_list_layout, R.id.nameTextView, debts){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);


                ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                TextView textView = (TextView) view.findViewById(R.id.nameTextView);
                TextView textView2 = (TextView) view.findViewById(R.id.amountTextView);
                TextView textView3 = (TextView) view.findViewById(R.id.contentTextView);
                imageView.setImageResource(android.R.drawable.btn_plus);
                textView.setText(debts.get(position).getDebtor().getName());
                textView2.setText("$" + String.format("%.02f", debts.get(position).getAmount()));
                textView3.setText("Net Amount Owed");
                return view;
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = getIntent();
            intent.putExtra("group", group);
            this.setResult(RESULT_OK, intent);
            finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        this.setResult(RESULT_OK, intent);
        intent.putExtra("group", group);
        finishAfterTransition();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
    }

    public List<Expense> sortExpenses(User user){
        List<Expense> userExpenseList= new ArrayList<>();
        List<Expense> expenses = group.getExpenses();
        for(int i = 0; i < expenses.size(); i++){
            if(user.getName().compareTo(expenses.get(i).getUserName()) == 0){
                userExpenseList.add(expenses.get(i));
            }
        }
        return userExpenseList;
    }

    public double totalExpenses(List<Expense> expenses){
        double total = 0;
        for(int i = 0; i < expenses.size(); i++){
           total += expenses.get(i).getAmount();
        }
        return total;
    }
}
