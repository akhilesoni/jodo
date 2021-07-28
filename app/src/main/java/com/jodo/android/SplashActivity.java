package com.jodo.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jodo.android.database.DBHelper;
import com.jodo.android.model.User;

public class SplashActivity extends AppCompatActivity {
    DBHelper dbHelper;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbHelper = new DBHelper(getApplicationContext());

        user = dbHelper.getUser();
        if(user==null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                    finish();
                }
            },1000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }, 1000);
        }

    }
}