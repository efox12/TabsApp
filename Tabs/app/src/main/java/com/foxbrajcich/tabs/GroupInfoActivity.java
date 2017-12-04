package com.foxbrajcich.tabs;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.foxbrajcich.tabs.R.id.pieChart;
import static com.foxbrajcich.tabs.R.id.spinner;

public class GroupInfoActivity extends AppCompatActivity {

    ArrayAdapter<Debt> adapter;
    ArrayAdapter<User> adapterTwo;
    List<Debt> debts;
    List<User> groupMembers = new ArrayList<>();
    List<Integer> colorList = new ArrayList<>();
    Group group;
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        group = (Group) getIntent().getSerializableExtra("group");
        groupMembers = group.getMembers();
        TextView totalGroupExpenseView = (TextView) findViewById(R.id.textView4);
        TextView totalUserExpenseView = (TextView) findViewById(R.id.textView5);
        totalGroupExpenseView.setText("Group Total: $" + String.format("%.02f",totalExpenses(group.getExpenses())));
        ListView listView = (ListView) findViewById(R.id.friendsList);
        List<User> otherUsers;

        addColorsToList();
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
        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.getLegend().setVisible(false);
        createSegments();
        pieChart.getBorderPaint().setColor(Color.TRANSPARENT);


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

        //get the list of debts that this user owes

        //if(localUser != null) {
        //    debts = group.getDebtsForUser(localUser);

        //}else{
           // debts = new ArrayList<>();
        //}
        totalUserExpenseView.setText("Your Expenses: $" + String.format("%.02f", group.getTotalExpenseForUser(group.getMembers().get(spinner.getSelectedItemPosition()))));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView totalUserExpenseView = (TextView) findViewById(R.id.textView5);
                debts = group.getDebtsForUser(groupMembers.get(i));
                totalUserExpenseView.setText("Your Expenses: $" + String.format("%.02f", group.getTotalExpenseForUser(group.getMembers().get(spinner.getSelectedItemPosition()))));
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

    public List<Segment> createSegments(){
        List<Segment> segmentList = new ArrayList<>();
        for(int i = 0; i < groupMembers.size(); i++){
            Segment segment = new Segment(groupMembers.get(i).getName(), group.getTotalExpenseForUser(group.getMembers().get(i)));
            SegmentFormatter formatter = new SegmentFormatter(colorList.get(i));
            //formatter.getRadialEdgePaint().setColor(Color.TRANSPARENT);
            //formatter.setRadialInset(10);
            formatter.getLabelPaint().setColor(Color.BLACK);
            pieChart.addSegment(segment, formatter);
        }
        return segmentList;
    }

    public double totalExpenses(List<Expense> expenses){
        double total = 0;
        for(int i = 0; i < expenses.size(); i++){
           total += expenses.get(i).getAmount();
        }
        return total;
    }
    public void addColorsToList(){
        colorList.add(Color.rgb(255,0,0));
        colorList.add(Color.rgb(55,156,0));
        colorList.add(Color.rgb(238,255,0));
        colorList.add(Color.rgb(30,255,0));
        colorList.add(Color.rgb(0,190,255));
        colorList.add(Color.rgb(73,198,229));
        colorList.add(Color.rgb(0,189,157));

    }

    public void startUpAnimation(){
        final PieRenderer pieRenderer = pieChart.getRenderer(PieRenderer.class);
        pieRenderer.setExtentDegs(0);
        pieRenderer.setDonutSize(0.5f, PieRenderer.DonutMode.PERCENT);

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = valueAnimator.getAnimatedFraction();
                pieRenderer.setExtentDegs(360 * scale);
                pieChart.redraw();
            }
        });

        animator.setDuration(1500);
        animator.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startUpAnimation();
    }
}
