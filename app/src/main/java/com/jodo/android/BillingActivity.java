package com.jodo.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jodo.android.adapters.ProductAdapter;
import com.jodo.android.adapters.TempProductAdapter;
import com.jodo.android.database.DBHelper;
import com.jodo.android.model.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class BillingActivity extends AppCompatActivity {
    TempProductAdapter adapter;
    List<Product> list;
    RecyclerView recyclerView;
    LinearLayout checkoutBtn;
    LinearLayout addbtn;
    ImageView empty,back,new_list;
    DBHelper dbHelper;

    private static final int REQUEST_EXTERNAL_STORAGe = 1;
    private static String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        dbHelper = new DBHelper(getApplicationContext());
        addbtn = (LinearLayout) findViewById(R.id.addItem);
        checkoutBtn = (LinearLayout) findViewById(R.id.checkoutBtn);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        empty = (ImageView) findViewById(R.id.empty_illustration);
        back = (ImageView) findViewById(R.id.back);
        new_list = (ImageView) findViewById(R.id.new_list);



        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
        verifystoragepermissions(this);
        updateList();
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CheckoutActivity.class));
           }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        new_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteAllTempProduct();
                updateList();
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
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()!=null){
                Log.d("jdskdsd", "onActivityResult: "+dbHelper.getData(result.getContents()));
                Product product = dbHelper.getData(result.getContents());
                if(product.getName()==null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BillingActivity.this);
                    builder.setTitle("Product Not Found!");
                    builder.setMessage("Want to add this in inventory?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(),AddProductActivity.class);
                            intent.putExtra("product_id",result.getContents());
                            startActivity(intent);
                            return;
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else {
                    dbHelper.inserttempProduct(product.getName(),product.getPrice(),product.getProduct_id());
                    updateList();
                }

            }else {
                Toast.makeText(this, "nothsbdsjd", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void updateList(){
        list = dbHelper.getAlltempProduct();
        if(list.size()!=0){
            empty.setVisibility(View.INVISIBLE);
        }else{
            empty.setVisibility(View.VISIBLE);


        }
        adapter = new TempProductAdapter(list,getApplicationContext());

        recyclerView.setAdapter(adapter);
    }

    protected static File screenshot(View view, String filename) {
        Date date = new Date();

        // Here we are initialising the format of our image name
        CharSequence format = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        try {
            // Initialising the directory of storage
            String dirpath = Environment.getExternalStorageDirectory() + "";
            File file = new File(dirpath);
            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }

            // File name
            String path = dirpath + "/" + filename + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            File imageurl = new File(path);
            FileOutputStream outputStream = new FileOutputStream(imageurl);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageurl;

        } catch (FileNotFoundException io) {
            io.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // verifying if storage permission is given or not
    public static void verifystoragepermissions(Activity activity) {

        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGe);
        }
    }

}