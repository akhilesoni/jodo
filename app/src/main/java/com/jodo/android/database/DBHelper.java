package com.jodo.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jodo.android.model.Product;
import com.jodo.android.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "jodo.db";
    public static final String PRODUCT_TABLE_NAME = "product";
    public static final String PRODUCT_COLUMN_ID = "id";
    public static final String PRODUCT_COLUMN_NAME = "name";
    public static final String PRODUCT_COLUMN_PRICE = "price";
    public static final String PRODUCT_COLUMN_PRODUCT_ID = "product_id";

    public static final String TEMP_PRODUCT_TABLE_NAME = "tempproduct";
    public static final String TEMP_PRODUCT_COLUMN_ID = "id";
    public static final String TEMP_PRODUCT_COLUMN_NAME = "name";
    public static final String TEMP_PRODUCT_COLUMN_PRICE = "price";
    public static final String TEMP_PRODUCT_COLUMN_PRODUCT_ID = "product_id";

    public static final String USER_COLUMN_ID = "id";

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_USERNAME = "username";
    public static final String USER_COLUMN_PASSWORD = "password";
    public static final String USER_COLUMN_SHOP_NAME = "shop_name";
    public static final String USER_COLUMN_ADDRESS = "address";
    public static final String USER_COLUMN_CONTACT = "contact";







    private HashMap hp;



    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table product" +
                        "(id integer primary key, name text,price integer,product_id text)"
        );
        db.execSQL(
                "create table tempproduct" +
                        "(id integer primary key, name text,price integer,product_id text)"
        );

        db.execSQL(
                "create table user" +
                        "(id integer primary key, username text,password text,shop_name text,address text,contact text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS product");
        db.execSQL("DROP TABLE IF EXISTS tempproduct");
        db.execSQL("DROP TABLE IF EXISTS user");

        onCreate(db);
    }



    public boolean insertProduct (String name, Integer price,String product_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("product_id", product_id);

        db.insert("product", null, contentValues);
        return true;
    }

    public boolean insertUser (String username, String password,String shop_name,String address,String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("shop_name", shop_name);
        contentValues.put("address",address);
        contentValues.put("contact",contact);

        db.insert("user", null, contentValues);
        return true;
    }

    public Integer deleteTempProduct (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tempproduct",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteProduct (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("product",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteAllTempProduct () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tempproduct",
                null,null);
    }

    public Integer deleteAllUser () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("user",
                null,null);
    }
    public boolean inserttempProduct (String name, Integer price,String product_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("product_id", product_id);

        db.insert("tempproduct", null, contentValues);
        return true;
    }

    public List<Product> getAllProduct() {
        List<Product> list = new ArrayList<Product>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from product", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Product product = new Product();
            product.setId(res.getInt(res.getColumnIndex(PRODUCT_COLUMN_ID)));
            product.setName(res.getString(res.getColumnIndex(PRODUCT_COLUMN_NAME)));
            product.setPrice(res.getInt(res.getColumnIndex(PRODUCT_COLUMN_PRICE)));
            product.setProduct_id(res.getString(res.getColumnIndex(PRODUCT_COLUMN_PRODUCT_ID)));

            list.add(product);
            res.moveToNext();
        }
        return list;
    }

    public User getUser() {
        User user = new User();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user", null );

        if(!res.moveToFirst()){
            user = null;
            return user;
        }
        user.setId(res.getInt(res.getColumnIndex(USER_COLUMN_ID)));
        user.setUsername(res.getString(res.getColumnIndex(USER_COLUMN_USERNAME)));
        user.setShop_name(res.getString(res.getColumnIndex(USER_COLUMN_SHOP_NAME)));
        user.setAddress(res.getString(res.getColumnIndex(USER_COLUMN_ADDRESS)));
        user.setPassword(res.getString(res.getColumnIndex(USER_COLUMN_PASSWORD)));
        user.setContact(res.getString(res.getColumnIndex(USER_COLUMN_CONTACT)));

        return user;
    }

    public List<Product> getAlltempProduct() {
        List<Product> list = new ArrayList<Product>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from tempproduct", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Product product = new Product();
            product.setId(res.getInt(res.getColumnIndex(TEMP_PRODUCT_COLUMN_ID)));
            product.setName(res.getString(res.getColumnIndex(TEMP_PRODUCT_COLUMN_NAME)));
            product.setPrice(res.getInt(res.getColumnIndex(TEMP_PRODUCT_COLUMN_PRICE)));
            product.setProduct_id(res.getString(res.getColumnIndex(TEMP_PRODUCT_COLUMN_PRODUCT_ID)));

            list.add(product);
            res.moveToNext();
        }
        return list;
    }
    public Product getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from product where product_id="+id+"", null );
        res.moveToFirst();
        Product product;

        if(!res.moveToFirst()){
            product = new Product(null,null,null,null);

        }
        else {
            product = new Product();
            product.setId(res.getInt(res.getColumnIndex(PRODUCT_COLUMN_ID)));
            product.setPrice(res.getInt(res.getColumnIndex(PRODUCT_COLUMN_PRICE)));
            product.setName(res.getString(res.getColumnIndex(PRODUCT_COLUMN_NAME)));
            product.setProduct_id(res.getString(res.getColumnIndex(PRODUCT_COLUMN_PRODUCT_ID)));
        }

        return product;
    }
}
