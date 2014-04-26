package com.example.licationremindersv1;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static java.net.URLEncoder.encode;


public class FindLocation implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,LocationListener {

    public static interface OnLocationUpdateCallbacks {
        void OnLocationUpdate(Location location);
    }
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationClient mLocationClient;
    private Context context;
    private OnLocationUpdateCallbacks callbacks;
    Location mCurrentLocation;
    private String dest;


    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    LocationRequest mLocationRequest;

    public FindLocation(Context context, OnLocationUpdateCallbacks callbacks){
        this.context = context;
        this.callbacks = callbacks;
    }

    public void start() {
        Log.d("FindLocation", "starting...");
        mLocationClient = new LocationClient(this.context, this, this);
        mLocationClient.connect();
    }

    public void stop(){
        Log.d("FindLocation", "stopping...");
        mLocationClient.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("FindLocation", "connected");
        mCurrentLocation = mLocationClient.getLastLocation();
        callbacks.OnLocationUpdate(mCurrentLocation);
        setDestintionFull(this.dest, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

    }

    @Override
    public void onDisconnected() {
        Log.d("FindLocation", "disconnected");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("FindLocation", "connection failed");
        if (!connectionResult.hasResolution()) {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    private void showErrorDialog(int errorCode) {

    }
    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void setDestination(String dest){
        this.dest = dest;
    }
    public void setDestintionFull(String dest,double lat, double lon){
        this.dest = dest;
        FindLocationTask task = new FindLocationTask(dest,lat,lon);
        task.execute();
    }
    private class FindLocationTask extends AsyncTask<URL, Integer, JSONObject> {
        private String dest;
        private double lat, lon;
        public FindLocationTask(String destination, double lat, double lon){
            this.dest = destination;
            this.lat = lat;
            this.lon = lon;
        }
        @Override
        protected JSONObject doInBackground(URL... params) {
            Log.d("FindLocation","in doInBackground");
            URL url = null;
            String location = null;
            try {
                try {
                    location = encode(this.dest, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+this.lat+","+this.lon+"&radius=200&name="+location+"&sensor=false&key=AIzaSyBI1TsUmczwyvfKMCueoSXZe6NOSJijOtY\n");


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
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            try {
                if(json != null){
                    JSONArray jArray = json.getJSONArray("result");
                    JSONObject jObj = (JSONObject)jArray.get(0);
                    String name = jObj.getString("name");
                    String address = jObj.getString("vicinity");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}
