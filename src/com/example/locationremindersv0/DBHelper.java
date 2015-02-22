package com.example.locationremindersv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.locationremindersv1.Place;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {  
	
	public static final String DB_NAME = "list.sqlite"; 
    public static final String TB_NAME = "lists";
    public static final String ITEM_TB_NAME = "items";
    public static final int VERSION = 1;
    public static final String ID = "_id";
    public static final String STORE = "_store";  
    public static final String ITEM = "item";
    public static final String CHECKED_ITEM = "checked";
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
                 + CHECKED_ITEM + " integer ,"
        		 + DATE + " integer)");
        db.execSQL("CREATE TABLE items (_id integer primary key autoincrement, _store int ,item VARCHAR, checked integer)");
    }   
    
    public long addEntry(String store, String date) {
    	SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();   
        values.put(STORE, store);   
        values.put(DATE,date);
        long row=db.insert(DBHelper.TB_NAME, null, values);
        db.close();
        return row;
    }

    public long addItemsEntry(String store, String item, int checked) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STORE, store);
        values.put(ITEM, item);
        values.put(CHECKED_ITEM,checked);
        long row=db.insert(DBHelper.ITEM_TB_NAME, null, values);
        db.close();
        return row;
    }

    public void updateChecked(String store, boolean c){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CHECKED_ITEM,c);
        String [] whereArgs = new String[1];
        whereArgs[0] = store;
        long row = db.update(DBHelper.ITEM_TB_NAME,values,"_store=?",whereArgs);
        db.close();
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
	 
	 public Cursor selectStore(){
	        SQLiteDatabase db = this.getReadableDatabase();
	        //Cursor cursor = db.query(TB_NAME, null, null, null,null, null, null);
	        Cursor cursor= db.rawQuery("SELECT _store FROM lists", null);
	        return cursor;
	 }
    
	 public Cursor selectItems(){
	        SQLiteDatabase db = this.getReadableDatabase();
	        //Cursor cursor = db.query(TB_NAME, null, null, null,null, null, null);
	        Cursor cursor= db.rawQuery("SELECT * FROM items", null);
	        return cursor;
	 }
     public Cursor selectChecked(){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT checked FROM items",null);
            return cursor;
     }
     public ArrayList<Place> getAll(){
         ArrayList<Place> list = new ArrayList<Place>();
         Cursor cursor = selectItems();
         while(cursor.moveToNext()){
             String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
             String item = cursor.getString(cursor.getColumnIndexOrThrow("item"));
             boolean checked = cursor.getInt(cursor.getColumnIndexOrThrow("checked")) == 1;
             int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
             Place p = new Place(cursor.getString(cursor.getColumnIndexOrThrow("_store")),
                     date, item, checked, id);
             list.add(p);

         }
         return list;
     }
}  


