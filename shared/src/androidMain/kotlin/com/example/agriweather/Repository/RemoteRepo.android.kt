package com.example.agriweather.Repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkPermission
import com.example.agriweather.Models.UserLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

actual suspend fun getCurrentLocation(): UserLocation? {
    return LocationProvider().getLocation()
}


internal class LocationProvider : KoinComponent {

    private val context: Context by inject()
    fun checkLocationPermission() : Boolean{
        val fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION

        return ContextCompat.checkSelfPermission(context, fineLocationPermission) == PackageManager.PERMISSION_GRANTED ||   ContextCompat.checkSelfPermission(context, coarseLocationPermission) == PackageManager.PERMISSION_GRANTED
    }
    suspend fun getLocation(): UserLocation? {


        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return suspendCancellableCoroutine { cont ->
            try {
                @SuppressLint("MissingPermission")
                val task = fusedLocationClient.lastLocation
                task.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        cont.resume(UserLocation(location.latitude, location.longitude))
                    } else {
                        cont.resume(null)
                    }
                }
                task.addOnFailureListener { e ->
                    cont.resumeWithException(e)
                }
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
    }
}

actual suspend fun checkLocationPermission(): Boolean {
    return LocationProvider().checkLocationPermission()
}