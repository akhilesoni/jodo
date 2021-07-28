package com.jodo.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jodo.android.adapters.ProductAdapter;
import com.jodo.android.database.DBHelper;
import com.jodo.android.model.Product;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView back;
    ProductAdapter adapter;
    List<Product> list;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        back = (ImageView) findViewById(R.id.back);
        dbHelper = new DBHelper(getApplicationContext());

        list = new ArrayList<Product>();

        list = dbHelper.getAllProduct();

        adapter = new ProductAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }
}