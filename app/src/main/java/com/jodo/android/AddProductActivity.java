package com.jodo.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jodo.android.database.DBHelper;

public class AddProductActivity extends AppCompatActivity {
    RelativeLayout addBtn;
    ImageView back;
    DBHelper dbHelper;
    RelativeLayout addProductForm;
    View cover;
    EditText name,price;
    TextView add,cancel;
    String product_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        addBtn = (RelativeLayout) findViewById(R.id.add_product);
        back = (ImageView) findViewById(R.id.back);
        addProductForm = (RelativeLayout) findViewById(R.id.add_product_form);
        dbHelper = new DBHelper(getApplicationContext());
        cover = (View) findViewById(R.id.cover);

        name = (EditText) findViewById(R.id.name);
        price = (EditText) findViewById(R.id.price);
        add = (TextView) findViewById(R.id.addbtn);
        cancel = (TextView) findViewById(R.id.cancelBtn);
        product_id = "";

        if(getIntent().getStringExtra("product_id")!=null){
            String pid = getIntent().getStringExtra("product_id");
            openMenu(pid);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameValue = name.getText().toString();
                String priceValue = price.getText().toString();

                if(nameValue==null || priceValue==null){
                    name.setError("required");
                    price.setError("required");
                    return;
                }

                dbHelper.insertProduct(nameValue,Integer.parseInt(priceValue),product_id);
                closeMenu();
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductActivity.this);
                builder.setTitle("Product Added");
                builder.setMessage("do you want to add another product?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scan();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    public void scan(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        //start scanning
        scanIntegrator.setCaptureActivity(CaptureAct.class);
        scanIntegrator.setOrientationLocked(false);
        scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        scanIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
                openMenu(result.getContents());

            } else {
                Toast.makeText(this, "nothsbdsjd", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void closeMenu(){
        addProductForm.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.menu_close));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addProductForm.setVisibility(View.INVISIBLE);
                cover.setVisibility(View.INVISIBLE);
            }
        },300);

    }

    public void openMenu(String pd){
        addProductForm.setVisibility(View.VISIBLE);
        cover.setVisibility(View.VISIBLE);
        product_id =  pd;
    }

}