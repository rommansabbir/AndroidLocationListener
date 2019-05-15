package com.rommansabbir.locationlistenerandroid

import android.location.Location

interface LocationCallback {
    fun onLocationSuccess(location: Location)
}