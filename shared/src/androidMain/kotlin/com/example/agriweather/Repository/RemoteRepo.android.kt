package com.example.agriweather.Repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.agriweather.Models.UserLocation
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
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

    fun getLocationName(lat: Long, long: Long): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                address.locality ?: address.subAdminArea ?: address.adminArea ?: address.countryName
            } else {
                "Near You"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Near You"
        }

    }
}

actual suspend fun checkLocationPermission(): Boolean {
    return LocationProvider().checkLocationPermission()
}

actual fun getLocalTime(time: String): String {

    val apiTime = time
    val sdfUtc = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    sdfUtc.timeZone = TimeZone.getTimeZone("GMT")
    val date = sdfUtc.parse(apiTime)

    val sdfLocal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault())
    sdfLocal.timeZone = TimeZone.getDefault()
    val localTime = sdfLocal.format(date)
    return formatReadableTime(localTime)
}

fun formatReadableTime(input: String): String {
    val cleanedInput = input.replace("GMT", "").trim()

    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("MMMM dd, yyyy 'at' h:mm a", Locale.ENGLISH)

    val date = inputFormat.parse(cleanedInput)

    return date?.let { outputFormat.format(it) } ?: "Invalid date"
}


actual fun getLocationName(lat: Long, long: Long): String {
    return LocationProvider().getLocationName(lat, long)
}