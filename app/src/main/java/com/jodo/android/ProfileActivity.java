package com.jodo.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jodo.android.database.DBHelper;
import com.jodo.android.model.User;

public class ProfileActivity extends AppCompatActivity {
    TextView name,shop_name,address,contact,setting,analytics,delete;
    User user;
    DBHelper dbHelper;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView) findViewById(R.id.username);
        address = (TextView) findViewById(R.id.shop_address);
        shop_name = (TextView) findViewById(R.id.shop_name);
        contact = (TextView) findViewById(R.id.contact);
        delete = (TextView) findViewById(R.id.delete);
        setting = (TextView) findViewById(R.id.setting);
        analytics = (TextView) findViewById(R.id.analytics);
        back = (ImageView) findViewById(R.id.back);
        dbHelper = new DBHelper(getApplicationContext());

        user = dbHelper.getUser();

        name.setText(user.getUsername());
        shop_name.setText(user.getShop_name());
        address.setText(user.getAddress());
        contact.setText(user.getContact());


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }
}