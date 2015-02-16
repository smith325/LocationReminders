package com.example.locationremindersv1;

import java.util.ArrayList;

import com.example.locationremindersv0.DBHelper;
import com.example.locationremindersv0.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class GetContactsActivity extends Activity {
	SQLiteDatabase sqldb;  
	public static final String DB_NAME = "list.sqlite"; 
    public static final String TB_NAME = "lists";
    public static final int VERSION = 1; 
    private static final String ID = "_id";
    public static final String STORE = "_store";  
    public static final String ITEM = "item";
    private static final String DATE = "date";
    final DBHelper helper = new DBHelper(this);   
    private static final String [] columns ={"_id","_store","item"};
	 
    public ArrayList<String> getinfo = new ArrayList<String>();
   
	private Button addfriendsButton = null;
	private Button nextButton = null;
	private Button backButton = null;
	
	static final int REQUEST_SELECT_CONTACT = 1;
	public ArrayList<String> mContactsName = new ArrayList<String>();
	public ArrayList<String> mContactsPhone = new ArrayList<String>();
	public ArrayList<String> mContactsEmail = new ArrayList<String>();
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_contacts);
        
        addfriendsButton = (Button)findViewById(R.id.addfriends);
        
        addfriendsButton.setOnClickListener(new OnClickListener() {     
            public void onClick(View view) {           
            	selectContact(); 
            } 
        });  
        
        nextButton = (Button)findViewById(R.id.next);
        
        nextButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		setContentView(R.layout.checkbox_main);
        		DisplayViewList();
        	}
        });
        
        backButton = (Button)findViewById(R.id.back);
        
        backButton.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		finish();
        	}
        });
        
    }
	
	public void DisplayViewList() {
		Button share=(Button)findViewById(R.id.share);
	
	    sqldb = helper.getWritableDatabase(); 
	    
	    share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	sendSMS();
            }
        });
	        
		final ListView lv = (ListView) findViewById(R.id.lv);
		
		final Cursor cr = sqldb.query("lists", columns, null, null, null, null, null);
		String[] ColumnNames = cr.getColumnNames(); 
		
		@SuppressWarnings("deprecation")
		ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.checkbox_info,  
                cr, ColumnNames, new int[] { R.id.tv1, R.id.tv2, R.id.tv3}); 
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				view.setBackgroundResource(R.color.yellow);
				Cursor cursor = (Cursor) listView.getItemAtPosition(position);
				String storename = cursor.getString(cursor.getColumnIndexOrThrow("_store"));
				String itemname = cursor.getString(cursor.getColumnIndexOrThrow("item"));
				if(getinfo.indexOf(storename+" "+itemname+"; ") == -1) {
					getinfo.add(storename+" "+itemname+"; ");
				}	
			}
		});
	}
	   
    private void sendSMS(){ 
    	StringBuffer totalnumber = new StringBuffer();
        StringBuffer totalinfo = new StringBuffer();
        for(int j=0;j<getinfo.size();j++) {
        	totalinfo.append((j+1)+getinfo.get(j)+" ");
        }
       
        for(int i=0;i<mContactsPhone.size();i++) {
        	totalnumber.append(mContactsPhone.get(i)+";");
        } 

    	Intent messageIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+totalnumber));
    	messageIntent.putExtra("sms_body", totalinfo.toString());
    	startActivity(messageIntent);
    }
  
	public void updatelist() {
		ListView contactsList=(ListView)findViewById(R.id.lv);
		
		ArrayAdapter<String> arrayAdapter = 
				new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, 
						mContactsName);
		contactsList.setAdapter(arrayAdapter);
	}
	
	public void selectContact() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK);
	    pickContactIntent.setType(CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
	    if (pickContactIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(pickContactIntent, REQUEST_SELECT_CONTACT);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
	        Uri contactUri = data.getData();
	        String[] projection = new String[]{CommonDataKinds.Phone.DISPLAY_NAME,CommonDataKinds.Phone.NUMBER, CommonDataKinds.Email.ADDRESS};
	        Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
	        if (cursor != null) {
	        	while (cursor.moveToNext()) {
	        		int nameIndex = cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME);
	        		int phoneIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER);
	        		String name = cursor.getString(nameIndex);
	        		String number = cursor.getString(phoneIndex);
	        		if (mContactsName.indexOf(name+" <"+number+">") == -1) {
	        			mContactsName.add(name+" <"+number+">"+" ");
		        		mContactsPhone.add(number);
	        		}
	        	updatelist();
	        }
	       }
	   }
}}
