package com.example.locationremindersv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

public class DBHelper extends SQLiteOpenHelper {  
	
	public static final String DB_NAME = "list.sqlite"; 
    public static final String TB_NAME = "lists";
    public static final int VERSION = 1;
    private static final String ID = "_id";
    public static final String STORE = "_store";  
    public static final String ITEM = "item";
    private static final String DATE = "date";
    
    public DBHelper(Context context) {   
        super(context, DB_NAME, null, VERSION);
    }
    
    
    /**
     * should be invoke when you never use DBhelper
     * To release the database and etc.
     */
    public void Close() {
            this.getWritableDatabase().close();
    }
  
    public void onCreate(SQLiteDatabase db) { 
        db.execSQL("CREATE TABLE "    
        		 + TB_NAME + " (" 
        		 + ID + " integer primary key autoincrement, "  
        		 + STORE + " VARCHAR ,"
        		 + ITEM + " VARCHAR, "
        		 + DATE + " integer)");    
    }   
    
    public long addEntry(String store, String item, String dates) {
    	SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();   
        values.put(STORE, store);   
        values.put(ITEM, item); 
        values.put(DATE, dates);
        long row=db.insert(DBHelper.TB_NAME, null, values);
        db.close();
        return row;
    }
    
    public Cursor getEntry(String Store)
    {
    	SQLiteDatabase db=this.getReadableDatabase();
    	Cursor c=db.rawQuery( 
    		     "SELECT item FROM lists WHERE  _store = 'Store'", null); 
    	return c;
    }
    
    
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		 arg0.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
	     onCreate(arg0);
	}
	    
	 public Cursor selectAll(){
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.query(TB_NAME, null, null, null,null, null, null);
	        return cursor;
	 }
	 
	 public Cursor selectStore(){
	        SQLiteDatabase db = this.getReadableDatabase();
	        //Cursor cursor = db.query(TB_NAME, null, null, null,null, null, null);
	        Cursor cursor= db.rawQuery("SELECT _store FROM lists", null);
	        return cursor;
	 }
    
	 public Cursor selectItems(){
	        SQLiteDatabase db = this.getReadableDatabase();
	        //Cursor cursor = db.query(TB_NAME, null, null, null,null, null, null);
	        Cursor cursor= db.rawQuery("SELECT * FROM lists", null);
	        return cursor;
	 }
    
}  


