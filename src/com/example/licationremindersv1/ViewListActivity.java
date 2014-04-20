package com.example.licationremindersv1;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.locationremindersv0.DBHelper;
import com.example.locationremindersv0.R;

public class ViewListActivity extends Activity {
	
	//public static final String EXTRA_MESSAGE = "TRIP";
	SQLiteDatabase sqldb;  
	public static final String DB_NAME = "list.sqlite"; 
    public static final String TB_NAME = "lists";
    public static final int VERSION = 1; 
    private static final String ID = "_id";
    public static final String STORE = "_store";  
    public static final String ITEM = "item";
    private static final String DATE = "date";
    final DBHelper helper = new DBHelper(this); 
    private TextView result = null;  
    private Spinner spinner = null;  
    private ArrayAdapter<String> adapter = null;  
    private static final String [] choice ={"Date","Store","Item"};
	//private SimpleCursorAdapter dataAdapter;	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_view_main);
	    Button back=(Button)findViewById(R.id.back);
	    Button empty=(Button)findViewById(R.id.empty);
	    sqldb = helper.getWritableDatabase(); 
	    
	    result = (TextView)findViewById(R.id.result); 
	    spinner = (Spinner)findViewById(R.id.spinner);
	    adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,choice);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //style
	    
	    spinner.setAdapter(adapter);
	    spinner.setVisibility(View.VISIBLE); 
	    spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
	    	@Override  
	    	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
	    		// TODO Auto-generated method stub  
	    		//result.setText("Sort by:"+((TextView)arg1).getText());
	    		result.setText("Sort by:"+choice[arg2]);
	    		arg0.setVisibility(View.VISIBLE);
	    		//String select=choice[arg2];
	    		String select=adapter.getItem(arg2);
	    		if (select.equals("Store")) {
	    			sortlistbystore();
	    		} else if (select.equals("Date")) {
	    			sortlistbydate();
	    		} else if (select.equals("Item")) {
	    			sortlistbyitem();
	    		} 
	    }
	    
	    	@Override  
	    	public void onNothingSelected(AdapterView<?> arg0) {  
	    		// TODO Auto-generated method stub  
	    		//updatelistview();
	    	}  
	    });
	    
	    
	    //updatelistview();
						  
	    empty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	 int res = sqldb.delete("lists", null, null);
            	 if (res == 0) {  
                     Toast.makeText(ViewListActivity.this, "It's empty",  
                             Toast.LENGTH_SHORT).show();  
                 } else {  
                     Toast.makeText(ViewListActivity.this, "empty success"+res+"lines",  
                             Toast.LENGTH_SHORT).show();  
                 }  
                 updatelistview();
                 helper.onUpgrade(sqldb,1,2);//to keep id correctly
            }
        });
	    
	    back.setOnClickListener(new OnClickListener() {
	    	@Override
            public void onClick(View v) {
            	finish();
            }
        });
	}
	
	@Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        // Inflate the menu; this adds items to the action bar if it is present.  
        getMenuInflater().inflate(R.menu.main, menu);  
        return true; 
	}
	
	public void updatelistview() {  
		ListView lv = (ListView) findViewById(R.id.lv);
		
		final Cursor cr = sqldb.query("lists", null, null, null, null, null, null);
		String[] ColumnNames = cr.getColumnNames(); 
		
		@SuppressWarnings("deprecation")
		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_info,  
                cr, ColumnNames, new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4}); 
		lv.setAdapter(adapter); 
	}
		/*
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				// Get the cursor, positioned to the corresponding row in the result set
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				int listid = cursor.getInt(cursor.getColumnIndexOrThrow("_store"));
				String itemname = cursor.getString(cursor.getColumnIndexOrThrow("item"));
				String dates = cursor.getString(cursor.getColumnIndexOrThrow("date"));
						
				Intent intent=new Intent();	
				intent.setClass(ViewListActivity.this, ViewListActivity.class);
				intent.putExtra("_id", listid);
				intent.putExtra("name", itemname);
				intent.putExtra("date", dates);
				startActivity(intent);	
					
			}
		});
		 */ 
		public void sortlistbystore() {  
			ListView lv = (ListView) findViewById(R.id.lv);
			
			final Cursor cr = sqldb.query("lists", null, null, null, null, null, STORE);
			String[] ColumnNames = cr.getColumnNames(); 
			
			@SuppressWarnings("deprecation")
			ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_info,  
	                cr, ColumnNames, new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4}); 
			lv.setAdapter(adapter);
		
		}
		
		public void sortlistbydate() {  
			ListView lv = (ListView) findViewById(R.id.lv);
			
			final Cursor cr = sqldb.query("lists", null, null, null, null, null, DATE);
			String[] ColumnNames = cr.getColumnNames(); 
			
			@SuppressWarnings("deprecation")
			ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_info,  
	                cr, ColumnNames, new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4}); 
			lv.setAdapter(adapter);
		
		}
		
		public void sortlistbyitem() {  
			ListView lv = (ListView) findViewById(R.id.lv);
			
			final Cursor cr = sqldb.query("lists", null, null, null, null, null, ITEM);
			String[] ColumnNames = cr.getColumnNames(); 
			
			@SuppressWarnings("deprecation")
			ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_info,  
	                cr, ColumnNames, new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4}); 
			lv.setAdapter(adapter);
		
		}
}
