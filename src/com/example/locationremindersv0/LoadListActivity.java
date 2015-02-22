package com.example.locationremindersv0;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.OnNavigationListener;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoadListActivity extends Activity {
	int width=0;
	int height=0;
	AbsoluteLayout abslayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowManager manager = (WindowManager)this.getSystemService(this.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		width =display.getWidth();
		height=display.getHeight();
		setContentView(R.layout.activity_load_list);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		abslayout=new AbsoluteLayout (this);
        final DBHelper mDBHelper = new DBHelper(LoadListActivity.this);
        Cursor cursor=mDBHelper.selectStore();
        
        ArrayList<String> list = new ArrayList<String>();
        list.add(" ");
        while(cursor.moveToNext()){
			list.add(cursor.getString(0));
		}
        
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        
        
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){  
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {  
               
            	String selectStore=adapter.getItem(arg2);
            	if(selectStore!=" "){
            		TextView textStore=new TextView(LoadListActivity.this);
            		textStore.setText(adapter.getItem(arg2));
            		
            		AbsoluteLayout.LayoutParams textStoreLP=new AbsoluteLayout.LayoutParams(250, 100, width/2-50, 50);
            		abslayout.addView(textStore, textStoreLP);
            		setContentView(abslayout);
            
            		
            		String [] it = null;
            		//Cursor resultCursor = mDBHelper.getEntry(selectStore);
            		Cursor resultCursor = mDBHelper.selectItems();
            		while(resultCursor.moveToNext()){
            			if(resultCursor.getString(1).equals(selectStore))
            		
            				it=resultCursor.getString(2).split("#");
            				String a = resultCursor.getString(3);
                            Log.d("LoadListActivity", a);
            			
            		}
            		int Y_Value_Of_Checkbox=height/8;
            		for(int i=0; i<=it.length-1; i++){
            			CheckBox checkBox=new CheckBox(LoadListActivity.this);
            			checkBox.setText(it[i]);
            			AbsoluteLayout.LayoutParams checkBoxLP=new AbsoluteLayout.LayoutParams(250, 100, width/2-150, Y_Value_Of_Checkbox);
            			Y_Value_Of_Checkbox+=80;
            			abslayout.addView(checkBox, checkBoxLP);
            			setContentView(abslayout);
                        /*checkBox.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDBHelper.updateChecked(,true);
                            }
                        });*/
            	    }
            		Button BackButton= new Button(LoadListActivity.this);
            		BackButton.setText("Back");
            		BackButton.setBackgroundColor(0xffff9900);
            		AbsoluteLayout.LayoutParams BackLP=new AbsoluteLayout.LayoutParams(200, 200, width/2-100, height-600);
            		abslayout.addView(BackButton, BackLP);
            		BackButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        	finish();
                        }
                    });
            	}
            }  
            public void onNothingSelected(AdapterView<?> arg0) {  
              
            }  
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.load_list, menu);
		return true;
	}
	
	
	

}
