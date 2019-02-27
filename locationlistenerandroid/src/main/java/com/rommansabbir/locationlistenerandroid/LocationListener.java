package com.rommansabbir.locationlistenerandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.os.Handler;
import android.util.Log;

public class LocationListener extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSION_REQUEST_LOCATION = 0;
    private Context context;
    private static final String TAG = "LocationListener";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static String ERROR_MESSAGE = "LOCATION NULL";
    private LocationListenerCallbackInterface locationListenerCallbackInterface;
    private Handler handler;
    private long intervalTimeMillis;

    /**
     * Get context from parent activity
     * Instantiate FusedLocationProviderClient
     * Instantiate callback interface
     * Instantiate Handler
     * @param context
     */
    public LocationListener(Context context) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) context);
        locationListenerCallbackInterface = (LocationListenerCallbackInterface) context;
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Request permission result
     * If request granted, request for location update
     * If not, then request for permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            /**
             * Request for  permission.
             */
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /**
                 * Permission has been granted.
                 */
                getLocation();
            } else {
                /**
                 * Permission request was denied.
                 */
                requestLocationPermission();
            }
        }
    }

    /**
     * Request for location permission
     * If permission granted, request for location update
     */
    public void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
            getLocation();

        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
            getLocation();

        }
    }

    /**
     * Request for location permission
     * Check for self permission if need
     */
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        /**
                         * If success, notify callback interface
                         */
                        locationListenerCallbackInterface.onLocationSuccess(location);
                    } else {
                        /**
                         * If failed, notify callback interface
                         */
                        locationListenerCallbackInterface.onLocationFailure(ERROR_MESSAGE);
                    }
                }
            });
        } else {
            /**
             * If permission not granted
             * Request for permission
             */
            requestLocationPermission();
        }
    }

    /**
     * Get location after a specific interval time
     * This interval must be in milli second
     * Run on a separate thread
     * Check if handler is null or not
     * If not null, request for location update
     * @param intervalTimeMillis
     */
    public void getLocationPeriodic(final long intervalTimeMillis){
        if(handler != null){
            this.intervalTimeMillis = intervalTimeMillis;
            handler.post(runnable);
        }
    }

    /**
     * Declare a new runnable
     * Check for handler is null or not
     * If not, then run the job after the specific interval time
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run: ");
            if(handler != null){
                getLocation();
                handler.postDelayed(runnable, intervalTimeMillis);
            }
        }
    };

    /**
     * Destroy the callback after it usages for thread fail safe
     * If you don't call destroyCallback(), this may lead problem
     */
    public void destroyCallback(){
        if(handler != null){
            Log.d(TAG, "destroyCallback: ");
            locationListenerCallbackInterface = null;
            handler = null;
            context = null;
        }
    }

    /**
     * Callback interface for LocationListener
     */
    public interface LocationListenerCallbackInterface {
        void onLocationSuccess(Location location);
        void onLocationFailure(String ERROR_MESSAGE);
    }
}
