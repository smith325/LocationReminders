package com.example.locationremindersv0;


import java.util.ArrayList;
import java.util.Calendar;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateNewListActivity extends Activity {
	ArrayList<EditText> items=new ArrayList<EditText>();
	int Current_Y_Value=0;
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
		
		abslayout=new AbsoluteLayout (this);
		setContentView(abslayout);
		
		String DB_NAME = "listsDB.db";
        final DBHelper mDBHelper = new DBHelper(
        		CreateNewListActivity.this);
        
        //LayoutInflater inflater = (LayoutInflater)this.getSystemService
        	//      (this.LAYOUT_INFLATER_SERVICE);
        //Button CreateButton = (Button) inflater.inflate(R.drawable.button_style,null);
		Button CreateButton= new Button(this);
		CreateButton.setText("Create");
		AbsoluteLayout.LayoutParams CreateLP=new AbsoluteLayout.LayoutParams(250, 250, 200, height-500);
		CreateButton.setBackgroundColor(0xffff9900);
		abslayout.addView(CreateButton, CreateLP);
		
		Button CancelButton= new Button(this);
		CancelButton.setText("Cancel");
		AbsoluteLayout.LayoutParams CancelLP=new AbsoluteLayout.LayoutParams(250, 250, width-500, height-500);
		CancelButton.setBackgroundColor(0xffff9900);
		abslayout.addView(CancelButton, CancelLP);
		
		TextView TextLocation=new TextView(this);
		TextLocation.setText("Location");
		AbsoluteLayout.LayoutParams TextLocationLP=new AbsoluteLayout.LayoutParams(250, 100, width/2-60, height/15);
		abslayout.addView(TextLocation, TextLocationLP);
		
		final EditText EditLocation=new EditText(this);
		AbsoluteLayout.LayoutParams EditLocationLP=new AbsoluteLayout.LayoutParams(500, 100, width/2-250, height/15+50);
		abslayout.addView(EditLocation, EditLocationLP);
		
		TextView TextItem1=new TextView(this);
		TextItem1.setText("Item");
		AbsoluteLayout.LayoutParams TextItem1LP=new AbsoluteLayout.LayoutParams(250, 100, width/2-30, height/15+200);
		abslayout.addView(TextItem1, TextItem1LP);
		
		final EditText EditItem1=new EditText(this);
		AbsoluteLayout.LayoutParams EditItem1LP=new AbsoluteLayout.LayoutParams(250, 100, width/2-120, height/15+250);
		Current_Y_Value=height/15+250+50;
		abslayout.addView(EditItem1, EditItem1LP);
		items.add(EditItem1);

        if(EditLocation.getText()!= null){
        }


        CancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	finish();
            }
        });
		
		CreateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	StringBuffer s=new StringBuffer(EditItem1.getText());
            	for(int i=1; i<=items.size()-1; i++){
            		     s.append("#");
            		     s.append(items.get(i).getText());
            	}
            	Calendar date=Calendar.getInstance();
                String destination = EditLocation.getText().toString();
            	long id = mDBHelper.addEntry(destination, s.toString(),0, date.getTime().toString());

                finish();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_list, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.AddItem:
	        	addItem();
	            return true;
	    }
	    return false;
	}
	
	public void addItem(){
		
		Current_Y_Value+=120;
		TextView TextItem=new TextView(this);
		TextItem.setText("Item");
		AbsoluteLayout.LayoutParams TextItemLP=new AbsoluteLayout.LayoutParams(250, 100, width/2-30, Current_Y_Value);
		abslayout.addView(TextItem, TextItemLP);
		
		Current_Y_Value+=50;
		//EditText EditItem=new EditText(this);
		items.add(new EditText(this));
		AbsoluteLayout.LayoutParams EditItemLP=new AbsoluteLayout.LayoutParams(250, 100, width/2-120, Current_Y_Value);
		Current_Y_Value+=50;
		//abslayout.addView(EditItem, EditItemLP);
		abslayout.addView(items.get(items.size()-1), EditItemLP);
		//items.add(EditItem);
		
	}

}
