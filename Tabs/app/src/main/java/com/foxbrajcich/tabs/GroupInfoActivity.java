package com.foxbrajcich.tabs;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import static android.R.string.yes;
import static com.foxbrajcich.tabs.R.id.spinner;
import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class GroupInfoActivity extends AppCompatActivity {

    ArrayAdapter<Debt> adapter;
    ArrayAdapter<User> adapterTwo;
    List<Debt> debts;
    List<User> groupMembers = new ArrayList<>();
    Group group;
    PieChart pieChart;

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
        group = (Group) getIntent().getSerializableExtra("group");
        groupMembers = group.getMembers();
        ListView listView = (ListView) findViewById(R.id.friendsList);
        List<User> otherUsers;

        //find the object for the user who is logged in
        User localUser = null;
        for(User user : groupMembers) {
            if(user.getName().equals(UserSession.getName())){
                localUser = user;
                debts = group.getDebtsForUser(localUser);
                break;
            }else {
                debts = new ArrayList<>();
            }
        }

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.getLegend().setEnabled(false);
        createSegments();
        pieChart.animateY(1200, Easing.EasingOption.EaseInOutQuad);
        pieChart.setDragDecelerationFrictionCoef(.98f);
        Description description = new Description();
        description.setEnabled(false);
        pieChart.setDescription(description);

        final Spinner spinner = (Spinner) findViewById(R.id.groupMembersSpinner);
        adapterTwo = new ArrayAdapter<User>(this, android.R.layout.activity_list_item, android.R.id.text1, groupMembers) {
            @NonNull
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                ImageView textView1 = (ImageView) view.findViewById(android.R.id.icon);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text1);
                textView1.setImageResource(R.drawable.user);
                textView2.setText(groupMembers.get(position).getName());
                return view;
            }

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                ImageView textView1 = (ImageView) view.findViewById(android.R.id.icon);
                TextView textView2 = (TextView) view.findViewById(android.R.id.text1);
                textView1.setImageResource(R.drawable.user);
                textView2.setText(groupMembers.get(position).getName());
                return view;
            }


        };
        spinner.setAdapter(adapterTwo);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                debts = group.getDebtsForUser(groupMembers.get(i));
                adapter.notifyDataSetChanged();
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
                textView.setText(groupMembers.get(position).getName());
                textView2.setText("Contributed $" + String.format("%.02f",totalExpenses(sortExpenses(groupMembers.get(position)))));
                textView3.setText("You owe $");
                imageView.setImageResource(R.drawable.user);
                textView.setText(debts.get(position).getDebtor().getName());
                textView2.setText("$" + String.format("%.02f", debts.get(position).getAmount()));
                textView3.setText("Net Amount Owed");
                return view;
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                final String amount = String.format("%.02f", debts.get(position).getAmount());

                LinearLayout linearLayout = new LinearLayout(GroupInfoActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setPadding(10, 10, 10, 10);

                CheckBox checkBox = new CheckBox(GroupInfoActivity.this);
                checkBox.setText("Pay in full");
                checkBox.setChecked(false);

                final EditText editText = new EditText(GroupInfoActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText.setHint("$0.00");
                editText.setGravity(Gravity.RIGHT);
                linearLayout.addView(checkBox);
                linearLayout.addView(editText);
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if(!b){
                            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
                });

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isChecked()) {
                            editText.setText(amount);
                            editText.setEnabled(false);
                            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(editText.getRootView().getWindowToken(), 0);
                        }
                        else {
                            editText.setText("");
                            editText.setEnabled(true);
                            InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }
                });

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupInfoActivity.this);
                alertDialog.setCancelable(false);
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
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

                                if(group.isOnline()){
                                    //TODO code for online logic
                                }else {
                                    transaction.setSendingUsersName(((User) spinner.getSelectedItem()).getName());
                                    transaction.setReceivingUsersName(adapter.getItem(position).getDebtor().getName());
                                }

                                group.getTransactions().add(transaction);

                                if(group.isOnline()){
                                    //TODO code for online logic
                                }else {
                                    LocalDatabaseHelper.getInstance(GroupInfoActivity.this).addTransactionToGroup(group, transaction);
                                }

                                debts = group.getDebtsForUser((User) spinner.getSelectedItem());
                                adapter.notifyDataSetChanged();
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
        });

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
}
