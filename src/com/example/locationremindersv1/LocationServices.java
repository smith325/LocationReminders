package com.example.locationremindersv1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.locationremindersv0.DBHelper;
import com.example.locationremindersv0.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static java.net.URLEncoder.encode;

/**
 * Created by sesmith325 on 5/10/14.
 */
public class LocationServices extends IntentService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {


    private static final String ACTION_CALCULATE_DISTANCE = "com.example.locationremindersv1.ACTION_CALCULATE_DISTANCE";
    SQLiteDatabase sqldb;
    final DBHelper helper = new DBHelper(this);



    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    public LocationServices() {
        super("LocationServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(intent.hasExtra(com.google.android.gms.location.LocationServices.FusedLocationApi.KEY_LOCATION_CHANGED)){
            Location currentLocation = intent.getParcelableExtra(com.google.android.gms.location.LocationServices.FusedLocationApi.KEY_LOCATION_CHANGED);
            calculateDistanceTo(currentLocation);
            return;
        }



        if(mGoogleApiClient == null){
            Log.d("onHandleIntent","new locationClient");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(com.google.android.gms.location.LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        if(!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            Log.d("onHandleIntent","is connecting");
            mGoogleApiClient.connect();

        }
    }

    private Intent createViewListDetailIntent(Place place){

        Intent intent=new Intent();
        intent.setClass(this, ViewListDetail.class);
        intent.putExtra("_id", place.id);
        intent.putExtra("_store", place.name);
        intent.putExtra("item", place.item);
        intent.putExtra("date", place.date);

        return intent;

    }
    private void calculateDistanceTo(Location currentLocation) {

        ArrayList<Place> list = helper.getAll();

        for (Place place : list){
            JSONObject json = doGooglePlaceSearch(place.name,currentLocation);
            MakeNotification(currentLocation,json,place);
        }
    }

    private void MakeNotification(Location currentLocation, JSONObject json, Place place) {
        try {
            if(json != null){
                JSONArray jArray = json.getJSONArray("results");
                JSONObject jObj = (JSONObject)jArray.get(0);
                String name = jObj.getString("name");
                Log.d("FindLocation Place: ", name);

                String address = jObj.getString("vicinity");

                double lat = jObj.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                double lon = jObj.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                Log.d("FindLocation","lat: "+lat);
                Log.d("FindLocation","lon: "+lon);


                Location location = new Location("location");
                location.setLatitude(lat);
                location.setLongitude(lon);

                float distance = location.distanceTo(currentLocation);
                if(distance < 100){

                    Intent i = createViewListDetailIntent(place);
                    PendingIntent pending = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

                    Log.d("distanceto: ",""+distance);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setContentTitle("Location Nearby!")
                                    .setContentText(name + " is located at " + address)
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setContentIntent(pending)
                                    .setAutoCancel(true)
                                    .setWhen(System.currentTimeMillis())
                            ;

                    mBuilder.setDefaults(Notification.DEFAULT_ALL);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(place.id,mBuilder.build());
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        PendingIntent pending = PendingIntent.getService(this, 0, new Intent(this, LocationServices.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
        mLocationRequest.setSmallestDisplacement(75);
        mLocationRequest.setFastestInterval(30000);
        Location currentLocation = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        com.google.android.gms.location.LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, pending);
    }


    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this,LocationServices.class);
        i.setAction(ACTION_CALCULATE_DISTANCE);
        this.startService(i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("FindLocation", "connection failed");
    }

    private JSONObject doGooglePlaceSearch(String destination,Location currentLocation) {
        Log.d("FindLocation","in doInBackground");
        URL url = null;
        String placeName = null;
        try {
            try {
                placeName = encode(destination, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String location = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
            url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+ location
                    + "&radius=100&name=" + placeName
                    + "&sensor=false&key=AIzaSyBI1TsUmczwyvfKMCueoSXZe6NOSJijOtY\n");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection urlConnection = null;
        try {
            urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Referer","http://example.locationremindersv0.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        try {
            in = new BufferedInputStream(urlConnection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("FINDLOC","passing in");
        if (in != null){
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            try {
                Log.d("FINDLOC","in try/catch");
                while ((line = r.readLine()) != null) {
                    total.append(line);
                    Log.d("FindLocation-line",line);
                }
                try {
                    return new JSONObject(total.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LocationServices", "GoogleApiClient connection has been suspend");
    }

}
