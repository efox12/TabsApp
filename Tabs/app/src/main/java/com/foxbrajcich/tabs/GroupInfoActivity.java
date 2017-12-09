/* Sources
/* https://github.com/PhilJay/MPAndroidChart
/* used this api for pie chart
*/

package com.foxbrajcich.tabs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class GroupInfoActivity extends AppCompatActivity {

    ArrayAdapter<Debt> adapter;
    ArrayAdapter<User> adapterTwo;
    List<Debt> debts;
    List<User> groupMembers = new ArrayList<>();
    List<User> displayMembers = new ArrayList<>();
    Group group;
    PieChart pieChart;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final int[] COLORS = {
            rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db"), Color.rgb(193, 37, 82),
            Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31),
            Color.rgb(179, 100, 53),  Color.rgb(217, 80, 138), Color.rgb(254, 149, 7),
            Color.rgb(254, 247, 120), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209),
            Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80), Color.rgb(207, 248, 246),
            Color.rgb(148, 212, 212), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175),
            Color.rgb(42, 109, 130)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.group_info_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {

            }
        });

        group = (Group) getIntent().getSerializableExtra("group");

        groupMembers = group.getMembers();
        displayMembers = new ArrayList<>();
        for(User u : groupMembers){
            if(u.getUsername().length() < 1){
                displayMembers.add(u);
            }else if(u.getUsername().equals(UserSession.getUsername())){
                displayMembers.add(0, u);
            }
        }

        ListView listView = (ListView) findViewById(R.id.friendsList);
        List<User> otherUsers;
        ScrollView scrollView = (ScrollView) findViewById(R.id.groupInfoScrollView);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.smoothScrollTo(0, 0);
        debts = new ArrayList<>();

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.getLegend().setEnabled(false);
        createSegments();
        pieChart.animateY(1200, Easing.EasingOption.EaseInOutQuad);
        pieChart.setDragDecelerationFrictionCoef(.98f);
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);

        final Spinner spinner = (Spinner) findViewById(R.id.groupMembersSpinner);
        adapterTwo = new ArrayAdapter<User>(this, android.R.layout.activity_list_item, android.R.id.text1, displayMembers) {
            @NonNull
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                ImageView textView1 = (ImageView) view.findViewById(android.R.id.icon);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text1);
                textView1.setImageResource(R.drawable.user);
                User user = this.getItem(position);
                textView2.setText(user.getName() + (user.getUsername().equals(UserSession.getUsername()) ? " (me)" : ""));
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView textView1 = (ImageView) view.findViewById(android.R.id.icon);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text1);
                textView1.setImageResource(R.drawable.user);
                User user = this.getItem(position);
                textView2.setText(user.getName() + (user.getUsername().equals(UserSession.getUsername()) ? " (me)" : ""));
                return view;
            }


        };
        spinner.setAdapter(adapterTwo);
        debts.addAll(group.getDebtsForUser((User) spinner.getSelectedItem()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                debts.clear();
                debts.addAll(group.getDebtsForUser((User) adapterView.getItemAtPosition(i)));
                updateListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter = new ArrayAdapter<Debt>(this, R.layout.expense_list_layout, R.id.nameTextView, debts){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                TextView textView = (TextView) view.findViewById(R.id.nameTextView);
                TextView textView2 = (TextView) view.findViewById(R.id.amountTextView);
                TextView textView3 = (TextView) view.findViewById(R.id.contentTextView);
                imageView.setImageResource(R.drawable.user);

                textView.setText(this.getItem(position).getDebtor().getName());
                User userSelected = adapterTwo.getItem(spinner.getSelectedItemPosition());
                User debtor = this.getItem(position).getDebtor();

                if(this.getItem(position).getAmount() >= 0.01d) {
                    textView2.setTextColor(Color.RED);
                    textView2.setText("$" + String.format("%.02f", this.getItem(position).getAmount()));
                    if(userSelected.getUsername().equals(UserSession.getUsername())){
                        //use "you"
                        textView3.setText("Amount you owe");
                    }else{
                        textView3.setText("Amount " + userSelected.getName() + " owes");
                    }

                } else if (group.getDebtToUser(group.getDebtsForUser(this.getItem(position).getDebtor()), (User) spinner.getSelectedItem()).getAmount() >= 0.01d){
                    textView2.setTextColor(Color.rgb(0,100,0));
                    textView2.setText("$" + String.format("%.02f", group.getDebtToUser(group.getDebtsForUser(this.getItem(position).getDebtor()), (User) spinner.getSelectedItem()).getAmount()));
                    if(userSelected.getUsername().equals(UserSession.getUsername())){
                        //use "you"
                        textView3.setText("Amount you are owed");
                    }else{
                        textView3.setText("Amount " + userSelected.getName() + " is owed");
                    }

                } else {
                    textView2.setTextColor(Color.BLUE);
                    textView2.setText(("$" + String.format("%.02f", this.getItem(position).getAmount())));
                    textView3.setText("Even");
                }

                return view;
            }
        };
        listView.setAdapter(adapter);
        updateListView();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                if(debts.get(position).getAmount() > 0) {
                    final String amount = String.format("%.02f", debts.get(position).getAmount());
                    LinearLayout linearLayout = new LinearLayout(GroupInfoActivity.this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setPadding(10, 10, 10, 10);

                    CheckBox checkBox = new CheckBox(GroupInfoActivity.this);
                    checkBox.setText("Pay in full");
                    checkBox.setChecked(false);

                    final EditText editText = new EditText(GroupInfoActivity.this);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editText.setHint("$0.00");
                    editText.setGravity(Gravity.RIGHT);
                    linearLayout.addView(checkBox);
                    linearLayout.addView(editText);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            if (!b) {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                        }
                    });

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (compoundButton.isChecked()) {
                                editText.setText(amount);
                                editText.setEnabled(false);
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(editText.getRootView().getWindowToken(), 0);
                            } else {
                                editText.setText("");
                                editText.setEnabled(true);
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            }
                        }
                    });

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupInfoActivity.this);
                    alertDialog.setCancelable(false);
                    alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        }
                    });

                    alertDialog.setTitle("Log Payment")
                        .setView(linearLayout)
                        .setPositiveButton(("Confirm"), new DialogInterface.OnClickListener(){
                            // delete the note if clicked
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {

                                Transaction transaction = new Transaction();
                                transaction.setAmount(Double.valueOf(editText.getText().toString()));

                                transaction.setSendingUsersName(((User) spinner.getSelectedItem()).getName());
                                transaction.setSendingUsername(((User) spinner.getSelectedItem()).getUsername());
                                transaction.setReceivingUsersName(adapter.getItem(position).getDebtor().getName());
                                transaction.setReceivingUsername(adapter.getItem(position).getDebtor().getUsername());

                                List<Transaction> transactions= group.getTransactions();
                                transactions.add(transaction);
                                group.setTransactions(transactions);

                                if(group.isOnline()) addTransactionToFirebase(transaction, group);
                                else LocalDatabaseHelper.getInstance(GroupInfoActivity.this).addTransactionToGroup(group, transaction);

                                debts.clear();
                                debts.addAll(group.getDebtsForUser((User) spinner.getSelectedItem()));
                                updateListView();

                                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                                createSegments();
                                pieChart.animateY(1200, Easing.EasingOption.EaseInOutQuad);
                            }
                        })
                        .setNegativeButton(("Cancel"), new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                            }
                        });

                    alertDialog.show();

                }
                return true;
            }
        });

        //int totalHeight = 0;
        //for(int i = 0; i < adapter.getCount(); i++){
        //    View item = adapter.getView(i, null, listView);
         //   item.measure(0,0);
        //    totalHeight += item.getMeasuredHeight();
        //}
        //listView.setMinimumHeight(totalHeight);
        getListHeight(listView);
    }

    public static void getListHeight (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup viewGroup = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, viewGroup);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
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
            if(user.getName().equals(expenses.get(i).getUsersName())){
                userExpenseList.add(expenses.get(i));
            }
        }
        return userExpenseList;
    }

    public void createSegments(){
        List<PieEntry> entries = new ArrayList<>();
        for(int i = 0; i < groupMembers.size(); i++){
            String name = (groupMembers.get(i).getName());
            Double value = group.getTotalContributionForUser(group.getMembers().get(i));
            if(value > 0){
                PieEntry entry = new PieEntry(Double.valueOf(value).floatValue(), name);
                entries.add(entry);
            }
            //entry.setLabel(generateSegmentSpannableText(name, String.format("%.02f", value)).toString());

        }
        PieDataSet set = new PieDataSet(entries, "Expenses");
        set.setValueTextSize(10f);

        IValueFormatter formatter = new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format("$%.02f", value);
            }

        };
        set.setValueFormatter(formatter);
        set.setColors(COLORS);
        PieData data = new PieData(set);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setUsePercentValues(false);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    public double totalExpenses(List<Expense> expenses){
        double total = 0;
        for(int i = 0; i < expenses.size(); i++){
           total += expenses.get(i).getAmount();
        }
        return total;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Group Total\n$ "+ String.format("%.02f",totalExpenses(group.getExpenses())));
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 11, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 0, 11, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 11, 0);
        s.setSpan(new RelativeSizeSpan(1f), 11, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 11, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.RED), 11, s.length(), 0);
        return s;
    }

    private void addTransactionToFirebase(Transaction transaction, Group group){
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("groups").child(group.getGroupId()).child("transactions");

        Map<String, Object> transactionInfo = new HashMap<>();
        transactionInfo.put("content", transaction.getName());
        transactionInfo.put("amount", transaction.getAmount());
        transactionInfo.put("sendingUser", transaction.getSendingUsername());
        transactionInfo.put("sendingUsersName", transaction.getSendingUsersName());
        transactionInfo.put("receivingUser", transaction.getReceivingUsername());
        transactionInfo.put("receivingUsersName", transaction.getReceivingUsersName());

        groupRef.push().setValue(transactionInfo);
    }

    private void updateListView(){
        Spinner userSpinner = findViewById(R.id.groupMembersSpinner);
        User userSelected = adapterTwo.getItem(userSpinner.getSelectedItemPosition());
        if(userSelected.getUsername().equals(UserSession.getUsername())){
            ((TextView) findViewById(R.id.textView6)).setText("Your Debts");
        }else{
            ((TextView) findViewById(R.id.textView6)).setText(userSelected.getName() + "'s Debts");
        }
        adapter.notifyDataSetChanged();
    }

}
