package com.example.licationremindersv1;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static java.net.URLEncoder.encode;

/**
 * Created by sesmith325 on 5/10/14.
 */
public class LocationServices extends IntentService implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,LocationListener {


    private static final String ACTION_CALCULATE_DISTANCE = "com.example.licationremindersv1.ACTION_CALCULATE_DISTANCE";

    private LocationClient mLocationClient;

    public LocationServices() {
        super("LocationServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(mLocationClient == null){
            mLocationClient = new LocationClient(this, this, this);
        }

        if(!mLocationClient.isConnected() && !mLocationClient.isConnecting()){
            mLocationClient.connect();
        }

        String action = intent.getAction();
        if(action != null && action.equals(ACTION_CALCULATE_DISTANCE)){
            calculateDistanceTo();
        }
    }

    private void calculateDistanceTo() {
        Location currentLocation = mLocationClient.getLastLocation();

        JSONObject json = doGooglePlaceSearch("Octagon",currentLocation);
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
                    Log.d("distanceto: ",""+distance);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent i = new Intent(this,LocationServices.class);
        i.setAction(ACTION_CALCULATE_DISTANCE);
        this.startService(i);
    }

    @Override
    public void onDisconnected() {
        Log.d("FindLocation", "disconnected");
    }

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("FindLocation", "status changed: "+provider + " " +status);
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

}
