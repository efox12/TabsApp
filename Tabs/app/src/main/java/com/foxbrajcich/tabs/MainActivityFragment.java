package com.foxbrajcich.tabs;

/**
 * Created by erikfox on 11/22/17.
 */

import android.app.Activity;
import android.graphics.drawable.Drawable;
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
    View rootView;

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
        if(friends.size() == 0){
            TextView groupEmptyText = (TextView) rootView.findViewById(R.id.noFriendsText);
            groupEmptyText.setText("You have no friends...");
            rootView.findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.noFriendsText).setVisibility(View.GONE);
            rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.GONE);
        }
        friendsAdapter.notifyDataSetChanged();
    }
    public void updateGroups(){
        groups = onGetList.getGroupList();
        if(groups.size() == 0){
            TextView groupEmptyText = (TextView) rootView.findViewById(R.id.noFriendsText);
            groupEmptyText.setText("No Groups Created");
            rootView.findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.noFriendsText).setVisibility(View.GONE);
            rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.GONE);
        }
        groupsAdapter.notifyDataSetChanged();
    }
    public void updateTransactions(){
        transactions = onGetList.getTransactionList();
        if(transactions.size() == 0){
            TextView groupEmptyText = (TextView) rootView.findViewById(R.id.noFriendsText);
            groupEmptyText.setText("No recent transactions");
            rootView.findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.VISIBLE);
        } else {
            //rootView.findViewById(android.R.id.list).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.noFriendsText).setVisibility(View.GONE);
            rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.GONE);
        }
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
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
            friends = onGetList.getFriendList();
            if(friends.size() == 0){
                TextView groupEmptyText = (TextView) rootView.findViewById(R.id.noFriendsText);
                groupEmptyText.setText("You have no friends...");
                rootView.findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.noFriendsText).setVisibility(View.GONE);
                rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.GONE);
            }
                friendsAdapter = new ArrayAdapter<User>(super.getContext(), android.R.layout.simple_list_item_1, friends) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
                        //TextView textView2 = (TextView) view.findViewById(android.R.id.text2);
                        textView1.setText(friends.get(position).getUsername());
                        return view;
                    }
                };

                listView.setAdapter(friendsAdapter);
        }

        if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
            groups = onGetList.getGroupList();
            if(groups.size() == 0){
                TextView groupEmptyText = (TextView) rootView.findViewById(R.id.noFriendsText);
                groupEmptyText.setText("No Groups Created");
                rootView.findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.VISIBLE);
            } else {
                //rootView.findViewById(android.R.id.list).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.noFriendsText).setVisibility(View.GONE);
                rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.GONE);
            }
                groupsAdapter = new ArrayAdapter<Group>(super.getContext(), R.layout.group_list_layout, R.id.groupTextView, groups) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        ImageView imageView = (ImageView) view.findViewById(R.id.groupImageView);
                        TextView textView = (TextView) view.findViewById(R.id.groupTextView);
                        TextView textView1 = (TextView) view.findViewById(R.id.groupTextView2);
                        textView.setText(groups.get(position).getGroupTitle());
                        textView1.setText("Group");
                        imageView.setImageResource(R.drawable.game);
                        return view;
                    }
                };

            listView.setAdapter(groupsAdapter);
        }

        if(getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
            transactions = onGetList.getTransactionList();
            if(transactions.size() == 0){
                TextView groupEmptyText = (TextView) rootView.findViewById(R.id.noFriendsText);
                groupEmptyText.setText("No recent transactions");
                rootView.findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.VISIBLE);
            } else {
                rootView.findViewById(R.id.noFriendsText).setVisibility(View.GONE);
                rootView.findViewById(R.id.noFriendsSadFace).setVisibility(View.GONE);
            }
                transactionsAdapter = new ArrayAdapter<Transaction>(super.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1, transactions) {
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
