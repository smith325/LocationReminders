package com.example.locationremindersv0;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenuActivity extends Activity {
	private Button NewListButton = null;
	private Button LoadListButton = null;
	private Button ShareListButton = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		NewListButton= (Button)findViewById(R.id.NewListButton);
		LoadListButton= (Button)findViewById(R.id.LoadListButton);
		ShareListButton= (Button)findViewById(R.id.ShareListButton);
		
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
            
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

}
