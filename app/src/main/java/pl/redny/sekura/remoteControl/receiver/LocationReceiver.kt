package pl.redny.sekura.remoteControl.receiver

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import pl.redny.sekura.activity.ViewModel


class LocationReceiver(val locationManager: LocationManager,val viewModel: ViewModel) : LocationListener {

    override fun onLocationChanged(p0: Location?) {
        val time = getLastBestLocation()
        if (time != null) {
            viewModel.longitude = time.longitude
            viewModel.latitude = time.latitude
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    @SuppressLint("MissingPermission")
    private fun getLastBestLocation(): Location? {
        val locationGPS: Location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val locationNet: Location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val gpsLocationTime = locationGPS.time
        val netLocationTime = locationNet.time
        return if (0 < gpsLocationTime - netLocationTime) {
            locationGPS
        } else {
            locationNet
        }
    }
}