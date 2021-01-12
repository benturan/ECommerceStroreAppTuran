package com.bilgeadam.ecommercestroreappturan.util;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {


	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "sqllite_database";//database ad

	private static final String TABLE_NAME = "user_cart";
	private static String Id = "Id";
	private static String UserId = "UserId";
	private static String ProductId = "ProductId";
	private static String ProductName = "ProductName";
	private static String ProductImage = "ProductImage";
	private static String Tax = "Tax";
	private static String Quantity = "Quantity";
    private static String Limits = "Limits";
    private static String Stock = "Stock";
	private static String Price = "Price";
    private static String OldPrice = "OldPrice";

	
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ UserId + " TEXT,"
				+ ProductId + " TEXT,"
				+ ProductName + " TEXT,"
				+ ProductImage + " TEXT,"
				+ Tax + " TEXT,"
				+ Quantity + " TEXT,"
                + Limits + " TEXT,"
                + Stock + " TEXT,"
                + Price + " TEXT,"
				+ OldPrice + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);
	}


	

	public void delete(String ProductId){
		
		 SQLiteDatabase db = this.getWritableDatabase();
		 db.execSQL("delete from "+TABLE_NAME+" where ProductId='"+ProductId+"'");
		 db.close();
	}
	
	public void addCartProduct(String UserId, String ProductId,String ProductName,String ProductImage,String Tax,
							   String Quantity,String Limits, String Stock,String Price,String OldPrice) {
		SQLiteDatabase db = this.getWritableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE ProductId="+ProductId+" and UserId="+UserId;

		SQLiteDatabase dbexist = this.getReadableDatabase();
		Cursor cursor = dbexist.rawQuery(selectQuery, null);

		cursor.moveToFirst();
		if(cursor.getCount() > 0){

			ContentValues values = new ContentValues();
			values.put("UserId", UserId);
			values.put("ProductId", ProductId);
			values.put("ProductName", ProductName);
			values.put("ProductImage", ProductImage);
			values.put("Tax", Tax);
			values.put("Quantity", Quantity);
            values.put("Limits", Limits);
            values.put("Stock", Stock);
			values.put("Price", Price);
            values.put("OldPrice", OldPrice);
			db.update(TABLE_NAME, values, ProductId + " = ?",
					new String[] { String.valueOf(ProductId) });
		}
		else {
			ContentValues values = new ContentValues();
			values.put("UserId", UserId);
			values.put("ProductId", ProductId);
			values.put("ProductName", ProductName);
			values.put("ProductImage", ProductImage);
			values.put("Tax", Tax);
			values.put("Quantity", Quantity);
            values.put("Limits", Limits);
            values.put("Stock", Stock);
			values.put("Price", Price);
            values.put("OldPrice", OldPrice);
			db.insert(TABLE_NAME, null, values);
			db.close();
		}
	}
	
	
	public HashMap<String, String> cartProductDetail(int id){
		
		HashMap<String,String> cartProduct = new HashMap<String,String>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;
		 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
			cartProduct.put("UserId", cursor.getString(1));
			cartProduct.put("ProductId", cursor.getString(2));
			cartProduct.put("ProductName", cursor.getString(3));
			cartProduct.put("ProductImage", cursor.getString(4));
			cartProduct.put("Tax", cursor.getString(5));
			cartProduct.put("Quantity", cursor.getString(6));
            cartProduct.put("Limits", cursor.getString(7));
            cartProduct.put("Stock", cursor.getString(8));
			cartProduct.put("Price", cursor.getString(9));
            cartProduct.put("OldPrice", cursor.getString(10));
        }
        cursor.close();
        db.close();
		// return kitap
		return cartProduct;
	}
	
	public  ArrayList<HashMap<String, String>> userCartList(){

		
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = db.rawQuery(selectQuery, null);
	    ArrayList<HashMap<String, String>> userCartList = new ArrayList<HashMap<String, String>>();
	    
	    if (cursor.moveToFirst()) {
	        do {
	            HashMap<String, String> map = new HashMap<String, String>();
	            for(int i=0; i<cursor.getColumnCount();i++)
	            {
	                map.put(cursor.getColumnName(i), cursor.getString(i));
	            }

				userCartList.add(map);
	        } while (cursor.moveToNext());
	    }
	    db.close();
	    return userCartList;
	}
	
	public void cartProductUpdate(String ProductId,String Quantity) {
	   // SQLiteDatabase db = this.getWritableDatabase();
	   // ContentValues values = new ContentValues();
       // values.put("UserId", UserId);
       // values.put("ProductId", ProductId);
        //values.put("ProductName", ProductName);
       // values.put("ProductImage", ProductImage);
       // values.put("Tax", Tax);
        //values.put("Quantity", Quantity);
        //values.put("Limits", Limits);
        //values.put("Stock", Stock);
        //values.put("Price", Price);
       // values.put("OldPrice", OldPrice);
	   // db.update(TABLE_NAME, values, ProductId + " = ?",
	            //new String[] { String.valueOf(ProductId) });
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("update  "+TABLE_NAME+" set Quantity='"+Quantity+"' where ProductId='"+ProductId+"'");
		db.close();
	}
	
	public int getRowCount() { 

		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();
		// return row count
		return rowCount;
	}
	

	public void resetTables(){ 
		//Bunuda uygulamada kullanm�yoruz. T�m verileri siler. tabloyu resetler.
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
