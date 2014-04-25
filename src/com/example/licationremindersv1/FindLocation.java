package com.example.licationremindersv1;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;


public class FindLocation implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,LocationListener {

    public static interface OnLocationUpdateCallbacks {
        void OnLocationUpdate(Location location);
    }

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationClient mLocationClient;
    private Context context;
    private OnLocationUpdateCallbacks callbacks;
    Location mCurrentLocation;


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
}
