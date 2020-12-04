package com.dostal.loveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    final String prefNameFirstStart="firstappstart";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_container,new Main_Fragment()).commit();
        //firstlogin();

    }

    private boolean firstlogin() {
        SharedPreferences preferences =getSharedPreferences(prefNameFirstStart,MODE_PRIVATE);
        if (preferences.getBoolean(prefNameFirstStart,true)){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_container,new Start_Fragment()).commit();
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean(prefNameFirstStart,false);
            editor.commit();
            return true;
        }else{
            return false;
        }

    }
}