package com.foxbrajcich.tabs;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
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

        // create the scrollview and set its position to the top
        ScrollView scrollView = (ScrollView) findViewById(R.id.profileScrollView);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.smoothScrollTo(0, 0);

        // initialize the text views
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(UserSession.getName());
        TextView textView2 = (TextView) findViewById(R.id.textView4);
        textView2.setText(UserSession.getUsername());

        // a button to edit the user's profile
        TextView editProfileButton = (TextView) findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, R.string.notImplementedYet, Toast.LENGTH_SHORT).show();
            }
        });

        // an array adapter for the list of transactions
        final List<Transaction> transactions = getTransactions();
        ListView activityList = (ListView) findViewById(R.id.activityList);
        adapter = new ArrayAdapter<Transaction>(this, R.layout.expense_list_layout, R.id.nameTextView, transactions){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                // get views in list layout
                ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                TextView textView = (TextView) view.findViewById(R.id.nameTextView);
                TextView textView2 = (TextView) view.findViewById(R.id.amountTextView);
                TextView textView3 = (TextView) view.findViewById(R.id.contentTextView);
                imageView.setImageResource(R.drawable.user);
                // set views in list layout
                if(transactions.get(position).getReceivingUsersName().equals(UserSession.getName())){
                    // if you were paid
                    textView.setText(transactions.get(position).getSendingUsersName() + getString(R.string.paidYou));
                    textView2.setText(getString(R.string.$) + String.format("%.02f",transactions.get(position).getAmount()));
                    textView2.setTextColor(Color.rgb(0, 100, 0));
                    textView3.setText(getTransactions().get(position).getGroupName());
                } else if(transactions.get(position).getSendingUsersName().equals(UserSession.getName())){
                    // if you paid someone
                    textView.setText(getString(R.string.youPaid) + transactions.get(position).getReceivingUsersName());
                    textView2.setText(getString(R.string.$)+String.format("%.02f",transactions.get(position).getAmount()));
                    textView2.setTextColor(Color.RED);
                    textView3.setText(getTransactions().get(position).getGroupName());
                }
                return view;
            }
        };
        activityList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getListHeight(activityList);
    }

    /**
     * sets the height of the listview to the expanded list height
     * @param listView the listview
     */
    public static void getListHeight (ListView listView) {

        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        // add the height of each individual list item
        ViewGroup viewGroup = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, viewGroup);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // set the height of the whole list
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            // override transition
            finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //override transition
        overridePendingTransition(R.anim.fade_in, R.anim.slide_in_right);
        super.onBackPressed();
    }

    /**
     * gets a list of all transactions involving the user
     * @return the list
     */
    public List<Transaction> getTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        // sort through the groups
        for(int i = 0; i < UserSession.getGroupsList().size(); i++){
            Group group = UserSession.getGroupsList().get(i);
            //sort though the transactions
            for(int j = 0; j < group.getTransactions().size(); j++){
                if(group.getTransactions().get(j).getReceivingUsersName().equals(UserSession.getName())){
                    group.getTransactions().get(j).setGroupName(group.getGroupTitle());
                    transactions.add(group.getTransactions().get(j));
                } else if(group.getTransactions().get(j).getSendingUsersName().equals(UserSession.getName())){
                    group.getTransactions().get(j).setGroupName(group.getGroupTitle());
                    transactions.add(group.getTransactions().get(j));
                }
            }
        }
        return transactions;
    }
}
