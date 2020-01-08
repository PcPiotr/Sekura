package pl.redny.sekura.remoteControl.receiver

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import pl.redny.sekura.activity.ViewModel


class LocationReceiver(val locationManager: LocationManager, val viewModel: ViewModel) : LocationListener {

    override fun onLocationChanged(p0: Location?) {
        val time = getLastBestLocation()
        if (time != null) {
            viewModel.longitude = time.longitude
            viewModel.latitude = time.latitude
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        // Not implemented cos not needed
    }

    override fun onProviderEnabled(p0: String?) {
        // Not implemented cos not needed
    }

    override fun onProviderDisabled(p0: String?) {
        // Not implemented cos not needed
    }

    @SuppressLint("MissingPermission")
    private fun getLastBestLocation(): Location? {
        val locationGPS: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val locationNet: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        return if (locationGPS != null && locationNet != null) {
            val gpsLocationTime = locationGPS.time
            val netLocationTime = locationNet.time
            if (0 < gpsLocationTime - netLocationTime) {
                locationGPS
            } else {
                locationNet
            }
        } else if (locationGPS == null && locationNet != null) {
            locationNet
        } else if (locationGPS != null && locationNet == null) {
            locationGPS
        } else {
            null
        }

    }
}