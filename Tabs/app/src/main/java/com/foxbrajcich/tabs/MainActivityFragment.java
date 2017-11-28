package com.foxbrajcich.tabs;

/**
 * Created by erikfox on 11/22/17.
 */

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment{
    int fragmentNumber;
    List<User> friends = new ArrayList<>();
    List<Group> groups = new ArrayList<>();
    List<Transaction> transactions = new ArrayList<>();

    ArrayAdapter<User> friendsAdapter;
    ArrayAdapter<Group> groupsAdapter;
    ArrayAdapter<Transaction> transactionsAdapter;

    OnSectionSelected onSectionSelected;
    OnGetList onGetList;


    public interface OnSectionSelected{
        public void onGroupSelected(int position);
        public void onFriendSelected(int position);
        public void onTransactionSelected(int position);
    }

    public interface OnGetList{
        public List<Group> getGroupList();
        public List<User> getFriendList();
        public List<Transaction> getTransactionList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groups = onGetList.getGroupList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onGetList = (OnGetList) activity;
            onSectionSelected = (OnSectionSelected) activity;
        }
        catch (Exception e){

        }
    }

    public void updateFriends(){
        friends = onGetList.getFriendList();
        friendsAdapter.notifyDataSetChanged();
    }
    public void updateGroups(){
        groups = onGetList.getGroupList();
        groupsAdapter.notifyDataSetChanged();
    }
    public void updateTransactions(){
        transactions = onGetList.getTransactionList();
        transactionsAdapter.notifyDataSetChanged();
    }

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public MainActivityFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainActivityFragment newInstance(int sectionNumber) {
        MainActivityFragment fragment = new MainActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)
            onSectionSelected.onFriendSelected(position);
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2)
            onSectionSelected.onGroupSelected(position);
        else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3)
            onSectionSelected.onTransactionSelected(position);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
            friends = onGetList.getFriendList();
            friendsAdapter = new ArrayAdapter<User>(super.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, friends){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
                    textView1.setText(friends.get(position).getName());
                    textView2.setText("Friend " + position);
                    return view;
                }
            };

            listView.setAdapter(friendsAdapter);
        }

        if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
            groups = onGetList.getGroupList();
            groupsAdapter = new ArrayAdapter<Group>(super.getContext(), R.layout.group_list_layout, R.id.groupTextView, groups){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                    TextView textView = (TextView) view.findViewById(R.id.groupTextView);
                    TextView textView1 = (TextView) view.findViewById(R.id.groupTextView2);
                    textView.setText(groups.get(position).getGroupTitle());
                    textView1.setText("Group");
                    imageView.setImageResource(android.R.drawable.alert_dark_frame);
                    return view;
                }
            };

            listView.setAdapter(groupsAdapter);
        }

        if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){
            transactions = onGetList.getTransactionList();
            transactionsAdapter = new ArrayAdapter<Transaction>(super.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, transactions){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
                    textView1.setText(transactions.get(position).getName());
                    textView2.setText("Transaction " + position);
                    return view;
                }
            };

            listView.setAdapter(transactionsAdapter);
        }
        //adapter.notifyDataSetChanged();
        return rootView;
    }
}
