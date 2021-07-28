package com.jodo.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentResult;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.jodo.android.adapters.BillProductAdapter;
import com.jodo.android.database.DBHelper;
import com.jodo.android.model.Product;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private static com.itextpdf.text.Image image;
    Boolean isDownloaded=false;
    ImageView back,download,share;
    RecyclerView recyclerView;
    DBHelper dbHelper;
    List<Product> list;
    TextView total_amount;
    ScrollView scrollView;
    BillProductAdapter adapter;
    File myFile;
    int height;
    Integer total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        total_amount = (TextView) findViewById(R.id.total);
        back = (ImageView) findViewById(R.id.back);
        download = (ImageView) findViewById(R.id.download);
        total = 0;

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        dbHelper = new DBHelper(getApplicationContext());
        list = new ArrayList<Product>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        list = dbHelper.getAlltempProduct();
        for(Product product:list){
            total += product.getPrice();
        }
        total_amount.setText(total.toString());
        adapter = new BillProductAdapter(list,getApplicationContext());
        recyclerView.setAdapter(adapter);

        height  = list.size()*80+600;

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutActivity.this);
                builder.setTitle("Notice");
                builder.setMessage("Do you want to save the bill?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takeScreenshot();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BillingActivity.class));
                finish();
            }
        });


    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(scrollView,scrollView.getChildAt(0).getHeight(),scrollView.getChildAt(0).getWidth());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Rectangle pagesize = new Rectangle(scrollView.getWidth(), height);
            Document document = new Document();
            document.setPageSize(pagesize);
            String directoryPath = android.os.Environment.getExternalStorageDirectory().toString();

            PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/"+now+"p.pdf")); //  Change pdf's name.

            document.open();

            Image image = Image.getInstance(directoryPath + "/" + now +".jpg");  // Change image's name and extension.

            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

            document.add(image);
            document.close();

            Log.d("dire", "takeScreenshot: "+directoryPath);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        openDoc("/storage/emulated/0",now.toString());

    }

    public void openDoc(final String dire, final String file){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You Want To Open The PDF?");
        builder.setTitle("Bill Saved Successfully");
        builder.setPositiveButton("Open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myFile = new File(dire + "/"+file+"p.pdf");
                Uri path = FileProvider.getUriForFile(CheckoutActivity.this,BuildConfig.APPLICATION_ID+".provider",myFile);

                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP  | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pdfOpenintent.setDataAndType(path, "application/pdf");
                try {
                    isDownloaded=true;
                    startActivity(pdfOpenintent);
                }
                catch (ActivityNotFoundException e) {

                }
                dbHelper.deleteAllTempProduct();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private Bitmap getBitmapFromView(View view,int height,int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(isDownloaded){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
}