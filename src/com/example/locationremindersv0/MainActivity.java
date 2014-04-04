package com.example.locationremindersv0;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button MainMenuButton = null;
	public DBHelper mDBHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainMenuButton= (Button)findViewById(R.id.MainMenuButton);
		MainMenuButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	Intent intent = new Intent();
	                intent.setClass(MainActivity.this, MainMenuActivity.class);
	                startActivity(intent);
	            }
	        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
