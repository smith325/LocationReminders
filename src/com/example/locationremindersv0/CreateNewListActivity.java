package com.example.locationremindersv0;


import com.example.locationremindersv0.MainActivity;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CreateNewListActivity extends Activity {
	ArrayList<EditText> items=new ArrayList<EditText>();
	int Current_Y_Value=270;
	AbsoluteLayout abslayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		abslayout=new AbsoluteLayout (this);
		setContentView(abslayout);
		
		String DB_NAME = "listsDB.db";
        final DBHelper mDBHelper = new DBHelper(
        		CreateNewListActivity.this);
		
		Button CreateButton= new Button(this);
		CreateButton.setText("Create");
		AbsoluteLayout.LayoutParams CreateLP=new AbsoluteLayout.LayoutParams(200, 100, 110, 900);
		abslayout.addView(CreateButton, CreateLP);
		
		Button CancelButton= new Button(this);
		CancelButton.setText("Cancel");
		AbsoluteLayout.LayoutParams CancelLP=new AbsoluteLayout.LayoutParams(200, 100, 410, 900);
		abslayout.addView(CancelButton, CancelLP);
		
		TextView TextLocation=new TextView(this);
		TextLocation.setText("Location");
		AbsoluteLayout.LayoutParams TextLocationLP=new AbsoluteLayout.LayoutParams(250, 100, 300, 50);
		abslayout.addView(TextLocation, TextLocationLP);
		
		final EditText EditLocation=new EditText(this);
		AbsoluteLayout.LayoutParams EditLocationLP=new AbsoluteLayout.LayoutParams(500, 100, 120, 70);
		abslayout.addView(EditLocation, EditLocationLP);
		
		TextView TextItem1=new TextView(this);
		TextItem1.setText("Item");
		AbsoluteLayout.LayoutParams TextItem1LP=new AbsoluteLayout.LayoutParams(250, 100, 325, 240);
		abslayout.addView(TextItem1, TextItem1LP);
		
		final EditText EditItem1=new EditText(this);
		AbsoluteLayout.LayoutParams EditItem1LP=new AbsoluteLayout.LayoutParams(250, 100, 230, 260);
		abslayout.addView(EditItem1, EditItem1LP);
		items.add(EditItem1);
		
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
            	mDBHelper.addEntry(EditLocation.getText().toString(), s.toString()); 
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
		AbsoluteLayout.LayoutParams TextItemLP=new AbsoluteLayout.LayoutParams(250, 100, 325, Current_Y_Value);
		abslayout.addView(TextItem, TextItemLP);
		
		Current_Y_Value+=20;
		//EditText EditItem=new EditText(this);
		items.add(new EditText(this));
		AbsoluteLayout.LayoutParams EditItemLP=new AbsoluteLayout.LayoutParams(250, 100, 230, Current_Y_Value);
		//abslayout.addView(EditItem, EditItemLP);
		abslayout.addView(items.get(items.size()-1), EditItemLP);
		//items.add(EditItem);
		
	}

}
