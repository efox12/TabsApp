package com.foxbrajcich.tabs;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ArrayAdapter<Transaction> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(UserSession.getName());
        TextView textView2 = (TextView) findViewById(R.id.textView4);
        textView2.setText(UserSession.getUsername());

        TextView editProfileButton = (TextView) findViewById(R.id.editProfileButton);
        final List<Transaction> transactions = getTransactions();
        ListView activityList = (ListView) findViewById(R.id.activityList);
        adapter = new ArrayAdapter<Transaction>(this, R.layout.expense_list_layout, R.id.nameTextView, transactions){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                TextView textView = (TextView) view.findViewById(R.id.nameTextView);
                TextView textView2 = (TextView) view.findViewById(R.id.amountTextView);
                TextView textView3 = (TextView) view.findViewById(R.id.contentTextView);
                imageView.setImageResource(R.drawable.user);
                if(transactions.get(position).getReceivingUsersName() == UserSession.getName()){
                    textView.setText(transactions.get(position).getSendingUsersName() + " Paid you");
                    textView2.setText("$"+String.format("%.02f",transactions.get(position).getAmount()));
                    textView2.setTextColor(Color.rgb(0, 100, 0));
                    textView3.setText("Group Name");
                } else if(transactions.get(position).getSendingUsername() == UserSession.getName()){
                    textView.setText("You paid " + transactions.get(position).getReceivingUsersName());
                    textView2.setText("$"+String.format("%.02f",transactions.get(position).getAmount()));
                    textView2.setTextColor(Color.RED);
                    textView3.setText("Group Name");
                }
                return view;
            }
        };
        activityList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right);
        super.onBackPressed();
    }

    public List<Transaction> getTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        for(int i = 0; i < UserSession.getGroupsList().size(); i++){
            Group group = UserSession.getGroupsList().get(i);
            for(int j = 0; j < group.getTransactions().size(); j++){
                if(group.getTransactions().get(j).getReceivingUsersName() == UserSession.getName()){
                    transactions.add(group.getTransactions().get(j));
                } else if(group.getTransactions().get(j).getSendingUsername() == UserSession.getName()){
                    transactions.add(group.getTransactions().get(j));
                }
            }
        }
        return transactions;
    }
}
