package com.rommansabbir.locationlistenerandroid

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.os.Handler


@SuppressLint("StaticFieldLeak")
object LocationListener : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    @SuppressLint("StaticFieldLeak")
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var permissionCallback: PermissionCallback? = null
    private var handler: Handler? = null
    private lateinit var activity : Activity
    private val PERMISSION_REQUEST_LOCATION = 0
    private var runnable : Runnable? =null
    private const val permission = Manifest.permission.ACCESS_FINE_LOCATION

    fun setComponent(activity: Activity){
        this.activity = activity
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        permissionCallback = activity as PermissionCallback
        handler = Handler(Looper.getMainLooper())
        requestLocationPermission()
    }


    /**
     * This method handle user response to asked permission
     * if granted, notify via callback
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    fun processResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) : LocationListener{
        for(singlePer in permissions){
            if(permission == singlePer && requestCode == PERMISSION_REQUEST_LOCATION){
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) permissionCallback?.onPermissionRequest(true)
            }

        }
        return LocationListener
    }

    /**
     * Request for location permission
     * If permission granted, request for location update
     */
    private fun requestLocationPermission() : LocationListener{
        if (ActivityCompat.shouldShowRequestPermissionRationale((activity),Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions((activity),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_REQUEST_LOCATION)

        } else {
            ActivityCompat.requestPermissions((activity),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)

        }
        return LocationListener
    }

    /**
     * Request for location permission
     * @param callback, provide listener callback to get notified
     */
     fun getLocation(callback: LocationCallback){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener((activity)) { location ->
                if (location != null) {
                    callback.onLocationSuccess(location)
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    /**
     * Get location after a specific interval time
     * This interval time must be in milli second
     * @param intervalTimeMillis
     * @param callback, LocationCallback
     */
    fun getLocationPeriodic(intervalTimeMillis: Long, callback: LocationCallback){
        runnable = Runnable {
            if (handler != null) {
                getLocation(callback)
                handler?.postDelayed(runnable, intervalTimeMillis)
            } else{
                requestLocationPermission()
            }
        }
        handler?.post(runnable)
    }

    /**
     * Destroy the instance of Handler & Permission Callback
     */
    fun selfDestroy() : LocationListener{
        if (handler != null) {
            permissionCallback = null
            handler = null
        }
        return LocationListener
    }

}
