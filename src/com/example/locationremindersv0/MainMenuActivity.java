package com.example.locationremindersv0;

import com.example.licationremindersv1.GetContactsActivity;
import com.example.licationremindersv1.ShareListsActivity;
import com.example.licationremindersv1.ViewListActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
	private Button NewListButton = null;
	private Button LoadListButton = null;
	private Button ShareListButton = null;
	private Button ViewListButton = null;
	private Button ClearAllButton = null;
	SQLiteDatabase sqldb;  
	public static final String DB_NAME = "list.sqlite"; 
    public static final String TB_NAME = "lists";
    public static final int VERSION = 1;  
    final DBHelper helper = new DBHelper(this);
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		NewListButton= (Button)findViewById(R.id.NewListButton);
		LoadListButton= (Button)findViewById(R.id.LoadListButton);
		ShareListButton= (Button)findViewById(R.id.ShareListButton);
		ViewListButton = (Button)findViewById(R.id.ViewListButton);
		//ClearAllButton = (Button)findViewById(R.id.ClearAllButton);
		
		NewListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, CreateNewListActivity.class);
                startActivity(intent);
            }
        });
		
		LoadListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, LoadListActivity.class);
            	//intent.setClass(MainMenuActivity.this, CreateNewListActivity.class);
                startActivity(intent);
            }
        });
		
		ShareListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, GetContactsActivity.class);
                startActivity(intent);
            }
        });
		
		ViewListButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, ViewListActivity.class);
            	//intent.setClass(MainMenuActivity.this, CreateNewListActivity.class);
                startActivity(intent);
            }
        });
		/*
		ClearAllButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	sqldb = helper.getWritableDatabase();
            	helper.onUpgrade(sqldb,VERSION,VERSION);
            	Toast.makeText(MainMenuActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        });*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
