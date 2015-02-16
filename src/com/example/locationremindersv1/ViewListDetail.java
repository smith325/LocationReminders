package com.example.locationremindersv1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.net.Uri;
import com.example.locationremindersv0.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.plus.PlusShare;

public class ViewListDetail extends Activity {


    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



		setContentView(R.layout.view_listdetail);
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

        Button shareButton = (Button) findViewById(R.id.share_button);
        final Item finalViewItem = viewItem;
        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the Google+ share dialog with attribution to your app.
                Intent shareIntent = new PlusShare.Builder(ViewListDetail.this)
                        .setType("text/plain")
                        .setText(finalViewItem.getStore()+"\n"+finalViewItem.getItem())
                        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
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

		vItem.setItem(i.getStringExtra("item"));
		return vItem;
	}

	public void initView(Item item) {		
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}

