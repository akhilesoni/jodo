package com.jodo.android;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jodo.android.database.DBHelper;
import com.jodo.android.model.Product;
import com.jodo.android.model.User;

public class MainActivity extends AppCompatActivity {
    User user;
    RelativeLayout btn;
    TextView profile,setting,help,shop_name,shop_address;
    ImageView add_product,menu;
    LinearLayout inventory;
    DBHelper dbHelper;
    View cover;
    LinearLayout shop_open;
    RelativeLayout bottom_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom_menu = (RelativeLayout) findViewById(R.id.bottom_menu);
        cover = (View) findViewById(R.id.cover);
        profile = (TextView) findViewById(R.id.profile);
        shop_name = (TextView) findViewById(R.id.shop_name);
        shop_address = (TextView) findViewById(R.id.shop_address);
        shop_open = (LinearLayout) findViewById(R.id.shop_open);
        setting = (TextView) findViewById(R.id.setting);
        help = (TextView) findViewById(R.id.help);
        dbHelper = new DBHelper(getApplicationContext());

        user = dbHelper.getUser();
        shop_name.setText(user.getShop_name());
        shop_address.setText(user.getUsername());
        btn = (RelativeLayout) findViewById(R.id.scanbtn);
        add_product = (ImageView) findViewById(R.id.add_product);
        inventory = (LinearLayout) findViewById(R.id.inventory);
        menu = (ImageView) findViewById(R.id.menu_right);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BillingActivity.class));
            }
            });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddProductActivity.class));
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cover.setVisibility(View.VISIBLE);
                bottom_menu.setVisibility(View.VISIBLE);
                bottom_menu.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.menu_open));
            }
        });
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cover.setVisibility(View.INVISIBLE);
                bottom_menu.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.menu_close));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottom_menu.setVisibility(View.INVISIBLE);
                    }
                },300);

            }
        });
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),InventoryActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });

        shop_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            }
        });
    }

}