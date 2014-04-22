package com.example.licationremindersv1;

import java.util.ArrayList;

import com.example.locationremindersv0.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ShareListsActivity extends Activity {
	private Button btn_share; 
    private Button btn_back;
    static final int REQUEST_SELECT_CONTACT = 1;
	public ArrayList<String> mContactsName = new ArrayList<String>();
	
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.share_list);
        
        btn_share=(Button)findViewById(R.id.share);
        btn_back=(Button)findViewById(R.id.back);
        TextView aFriends = (TextView)findViewById(R.id.textView1);
        
        btn_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	showShareDialog();
            }
        });
        
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	finish();
            }
        });
    }
    
    private void showShareDialog(){  
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);    
              builder.setTitle("Choose your way to share:");    
              builder.setItems(new String[]{"Mail","Text"},  
                      new DialogInterface.OnClickListener() {    
                          public void onClick(DialogInterface dialog, int which) {    
                              dialog.dismiss();    
                              switch (which) {    
                              case 0:{    
                                  sendMail("Share APP with you");  
                                  break;      
                              }    
                              case 1:    
                                  sendSMS();  
                                  break;    
                              }    
                          }    
                      });    
               builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {    
                  @Override    
                  public void onClick(DialogInterface dialog, int which) {    
                      dialog.dismiss();    
                  }    
              });    
              builder.create().show();    
       }  
    
    private void sendMail(String emailBody){  
        Intent email = new Intent(android.content.Intent.ACTION_SEND);  
        email.setType("plain/text");  
        String  emailSubject = "Share APP with you";  
            
       // email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);  
 
        email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);  
  
        email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);  
     
        startActivityForResult(Intent.createChooser(email, "choose your mail APP"),1001);  
   }  
    
    private void sendSMS(){  
        Uri smsToUri = Uri.parse("smsto:");  
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);  
        sendIntent.putExtra("address", "12345678901");  
        sendIntent.putExtra("sms_body", "I want to recommend this APP!");  
        sendIntent.setType("vnd.android-dir/mms-sms");  
        startActivityForResult(sendIntent,1002);  
        }
}
