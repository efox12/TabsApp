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
    ArrayAdapter<String> friendsAdapter;
    ArrayAdapter<String> groupsAdapter;
    ArrayAdapter<String> transactionsAdapter;
    OnSectionSelected onSectionSelected;
    OnGetList onGetList;


    public interface OnSectionSelected{
        public void onGroupSelected(int position);
        public void onFriendSelected(int position);
        public void onTransactionSelected(int position);
    }

    public interface OnGetList{
        public List<String> getGroupList();
        public List<String> getFriendList();
        public List<String> getTransactionList();
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
        groups = onGetList.getGroupList();
        friendsAdapter.notifyDataSetChanged();
    }
    public void updateGroups(){
        groups = onGetList.getGroupList();
        groupsAdapter.notifyDataSetChanged();
    }
    public void updateTransactions(){
        groups = onGetList.getGroupList();
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
    List<String> friends = new ArrayList<>();
    List<String> groups = new ArrayList<>();
    List<String> transactions = new ArrayList<>();

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
            friendsAdapter = new ArrayAdapter<String>(super.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, friends){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
                    textView1.setText(friends.get(position) + " " + getArguments().getInt(ARG_SECTION_NUMBER));
                    textView2.setText(friends.get(position));
                    return view;
                }
            };

            listView.setAdapter(friendsAdapter);
        }

        if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
            groups = onGetList.getGroupList();
            groupsAdapter = new ArrayAdapter<String>(super.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, groups){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
                    textView1.setText(groups.get(position) + " " + getArguments().getInt(ARG_SECTION_NUMBER));
                    textView2.setText(groups.get(position));
                    return view;
                }
            };

            listView.setAdapter(groupsAdapter);
        }

        if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){
            transactions = onGetList.getTransactionList();
            transactionsAdapter = new ArrayAdapter<String>(super.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, transactions){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
                    TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
                    textView1.setText(transactions.get(position) + " " + getArguments().getInt(ARG_SECTION_NUMBER));
                    textView2.setText(transactions.get(position));
                    return view;
                }
            };

            listView.setAdapter(transactionsAdapter);
        }
        //adapter.notifyDataSetChanged();
        return rootView;
    }
}
