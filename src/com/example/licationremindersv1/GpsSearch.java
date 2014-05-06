package com.example.licationremindersv1;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import java.util.List;


public class GpsSearch extends IntentService{

    public GpsSearch(){
        super("GpsSearch");
    }

    public GpsSearch(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (LocationClient.hasError(intent)) {
            // skip
        } else {
            int transitionType = LocationClient.getGeofenceTransition(intent);
            if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                List<Geofence> triggerList = LocationClient.getTriggeringGeofences(intent);
                String name = intent.getExtras().getString("name");
                String address = intent.getExtras().getString("address");
                generateNotification(name,address);
            }
        }
    }
    private void generateNotification(String name, String address) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Location Nearby!")
                        .setContentText(name+" is located at "+address);

        NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());
    }
}
