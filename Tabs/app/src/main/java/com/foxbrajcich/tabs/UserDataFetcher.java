package com.foxbrajcich.tabs;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Robert on 12/4/2017.
 */

public class UserDataFetcher {

    private static FirebaseDatabase mFirebaseDatabase;
    private static Map<String, UserRequest> userRequestMap;

    static {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        userRequestMap = new HashMap<>();
    }

    private UserDataFetcher(){}

    public static void registerUserToPopulate(User user){
        if(userRequestMap.get(user.getUsername()) != null){
            //if the user request already exists
            userRequestMap.get(user.getUsername()).addUserForData(user);
        }else{
            //if there is not already an entry for this user
            UserRequest newRequest = new UserRequest();
            newRequest.addUserForData(user);
            userRequestMap.put(user.getUsername(), newRequest);
            mFirebaseDatabase.getReference("users").child(user.getUsername()).addListenerForSingleValueEvent(newRequest);
        }
    }

    private static class UserRequest implements ValueEventListener{

        private String name;
        private List<User> pendingUsers;
        private boolean dataFilled = false;

        UserRequest(){
            pendingUsers = new ArrayList<>();
        }

        void addUserForData(User u){
            if(dataFilled){
                u.setName(name);
            }else{
                pendingUsers.add(u);
            }
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();
                name = (String) userData.get("name");

                for(User u : pendingUsers){
                    u.setName(name);
                }

                pendingUsers.clear();
                dataFilled = true;
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

}
