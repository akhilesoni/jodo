package com.jodo.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jodo.android.database.DBHelper;
import com.jodo.android.model.User;

public class RegisterActivity extends AppCompatActivity {
    User user;
    DBHelper dbHelper;
    EditText username, address, shop_name,contact;
    LinearLayout register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.username);
        address = (EditText) findViewById(R.id.shop_address);
        shop_name = (EditText) findViewById(R.id.shop_name);
        contact = (EditText) findViewById(R.id.contact);
        register = (LinearLayout) findViewById(R.id.register);
        dbHelper = new DBHelper(getApplicationContext());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameText,contactText,addressText,shop_nameText;
                usernameText = username.getText().toString();
                contactText = contact.getText().toString();
                addressText = address.getText().toString();
                shop_nameText = shop_name.getText().toString();

                if(usernameText==null){
                    username.setError("Required");
                    return;
                }
                if(addressText==null){
                    username.setError("Required");
                    return;
                }
                if(shop_nameText==null){
                    username.setError("Required");
                    return;
                }
                if(contactText==null){
                    username.setError("Required");
                    return;
                }
                dbHelper.insertUser(usernameText,"password",shop_nameText,addressText,contactText);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));


            }
        });
    }
}