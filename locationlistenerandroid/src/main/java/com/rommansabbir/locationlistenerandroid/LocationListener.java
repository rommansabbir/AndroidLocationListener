package com.rommansabbir.locationlistenerandroid;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationListener extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int PERMISSION_REQUEST_LOCATION = 0;
    private Context context;
    private static final String TAG = "LocationListener";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static String ERROR_MESSAGE = "LOCATION NULL";
    private LocationListenerCallbackInterface locationListenerCallbackInterface;

    public LocationListener(Context context) {
        this.context = context;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) context);
        locationListenerCallbackInterface = (LocationListenerCallbackInterface) context;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            // Request for  permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.
                getLocation();
            } else {
                // Permission request was denied.
                requestLocationPermission();
            }
        }
    }

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

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        locationListenerCallbackInterface.onLocationSuccess(location);
                    } else {
                        locationListenerCallbackInterface.onLocationFailure(ERROR_MESSAGE);
                    }
                }
            });
        } else {
            requestLocationPermission();
        }
    }

    public interface LocationListenerCallbackInterface {
        void onLocationSuccess(Location location);
        void onLocationFailure(String ERROR_MESSAGE);
    }
}
