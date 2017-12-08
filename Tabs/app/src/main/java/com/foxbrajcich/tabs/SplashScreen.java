package com.foxbrajcich.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.foxbrajcich.tabs.LoginActivity.PREFERENCES_FILE;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseDatabase.getInstance().getReference("dummy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences preferences = SplashScreen.this.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

                String storedName =  preferences.getString("name", "");
                String storedUsername = preferences.getString("username", "");

                if(storedName != "" && storedUsername != ""){
                    logUserIn(storedName, storedUsername);
                }else{
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finishAfterTransition();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void logUserIn(String name, String username){

        UserSession.clearUserSession();

        UserSession.setName(name);
        UserSession.setUsername(username);

        FirebaseDatabase.getInstance().getReference("users").child(username).child("friends").addListenerForSingleValueEvent(new FriendsListLoader());
    }

    private class FriendsListLoader implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(!dataSnapshot.exists()){
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finishAfterTransition();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return;
            }

            Map<String, Object> usernames = (Map<String, Object>) dataSnapshot.getValue();

            for(String randomId : usernames.keySet()){
                User newFriend = new User("", (String) usernames.get(randomId));
                UserDataFetcher.registerUserToPopulate(newFriend); //set up user populator to grab name of user and fill it
                UserSession.addFriend(newFriend);
            }

            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finishAfterTransition();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
