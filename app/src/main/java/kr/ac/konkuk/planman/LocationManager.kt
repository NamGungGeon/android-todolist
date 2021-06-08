package kr.ac.konkuk.planman

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class LocationManager {
    companion object {
        private var inst: LocationManager? = null
        fun getInstance(): LocationManager {
            if (inst == null)
                inst = LocationManager()
            return inst!!
        }
    }

    private var locationClient: FusedLocationProviderClient? = null
    private fun startLocationClient(context: Context): FusedLocationProviderClient? {
        if (!checkRequiredPermission(context))
            return null

        if (locationClient == null)
            locationClient = LocationServices.getFusedLocationProviderClient(context)
        return locationClient
    }

    private fun observeClient(context: Context): FusedLocationProviderClient? {
        if (!checkRequiredPermission(context))
            return null
        if (startLocationClient(context) == null)
            return null

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationClient!!.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                }
            }, Looper.myLooper()
        )
        return locationClient
    }

    fun useLastLocation(context: Context, callback: (Location?) -> Unit) {
        if (!checkRequiredPermission(context)) {
            callback(null)
            return
        }
        if (observeClient(context) == null) {
            callback(null)
            return
        }
        locationClient?.lastLocation?.addOnSuccessListener { location ->
            callback(location)
        }
    }

    fun checkRequiredPermission(context: Context): Boolean {
        return (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun requirePermission(activity: Activity, requestCode: Int): Boolean {
        if (!checkRequiredPermission(activity)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(activity, "앱 실행을 위해서는 권한을 설정해야 합니다", Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    requestCode
                )
            }
        }
        return checkRequiredPermission(activity)
    }
}