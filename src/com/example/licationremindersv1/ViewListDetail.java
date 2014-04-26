package com.example.licationremindersv1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.location.Location;
import com.example.locationremindersv0.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewListDetail extends Activity implements FindLocation.OnLocationUpdateCallbacks {
    FindLocation locFinder;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locFinder = new FindLocation(this, this);

		setContentView(R.layout.view_listdetail);
		Button backButton=(Button)findViewById(R.id.backButton);
		Item viewItem = new Item();
		try {
			viewItem = getItemFromIntent(getIntent());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(viewItem.getStore().equals(" ")) {
			 TextView errorView=(TextView)findViewById(R.id.errorview);
			 errorView.setVisibility(View.VISIBLE);
		}
		else{ 
				initView(viewItem);
		}
		
		backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	finish();
            }
        });
	}
	
	public Item getItemFromIntent(Intent i) throws ParseException {
		i = getIntent();
		Item vItem =new Item(); 
		vItem.setId(i.getIntExtra("_id", 0));
		
		SimpleDateFormat df= new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy");
		Calendar cal1= Calendar.getInstance();
		Date dater = df.parse(i.getStringExtra("date"));
		cal1.setTime(dater);
		vItem.setDate(cal1);
		
		vItem.setStore(i.getStringExtra("_store"));

        locFinder.setDestination(i.getStringExtra("_store"));
		
		vItem.setItem(i.getStringExtra("item"));
		return vItem;
	}

	public void initView(Item item) {		
		// TODO - fill in here
		TextView timeView=(TextView)findViewById(R.id.textView3);
		timeView.setVisibility(View.VISIBLE);
		TextView locationView=(TextView)findViewById(R.id.textView4);
		locationView.setVisibility(View.VISIBLE);
		TextView name1View=(TextView)findViewById(R.id.textView2);
		name1View.setVisibility(View.VISIBLE);

        TextView viewTime=(TextView)findViewById(R.id.viewtime);
		viewTime.setText(item.getDate().getTime().toString());
		viewTime.setVisibility(View.VISIBLE);
	
		TextView viewLocation=(TextView)findViewById(R.id.viewdestination);
		viewLocation.setText(item.getItem());
		viewLocation.setVisibility(View.VISIBLE);
		
		TextView viewName=(TextView)findViewById(R.id.viewname);
		viewName.setText(item.getStore());
		viewName.setVisibility(View.VISIBLE);
	}

    @Override
    protected void onStart() {
        super.onStart();
        locFinder.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locFinder.stop();
    }

    @Override
    public void OnLocationUpdate(Location location) {
        TextView viewGeoLocation=(TextView)findViewById(R.id.fillLocation);
        viewGeoLocation.setText(location.toString());
        viewGeoLocation.setVisibility(View.VISIBLE);
    }
}

