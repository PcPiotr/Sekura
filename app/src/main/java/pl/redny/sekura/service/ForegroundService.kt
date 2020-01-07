package pl.redny.sekura.service

import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.koin.android.ext.android.inject
import pl.redny.sekura.activity.MainActivity
import pl.redny.sekura.activity.ViewModel
import pl.redny.sekura.remoteControl.receiver.LocationReceiver
import pl.redny.sekura.remoteControl.receiver.SmsBroadcastReceiver


class ForegroundService : Service() {
    val CHANNEL_ID = "ForegroundSekuraServiceChannel"
    private val broadcastReceiver: SmsBroadcastReceiver by inject()
    private val intentFilter = IntentFilter()
    private val viewModel: ViewModel by inject()
    private var locationReceiver: LocationReceiver? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(broadcastReceiver, intentFilter)
        locationReceiver = LocationReceiver(getSystemService(Context.LOCATION_SERVICE) as LocationManager, viewModel)
        if ( this.checkSelfPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            locationReceiver!!.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationReceiver)
            locationReceiver!!.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationReceiver)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Binder()

    }

    @RequiresApi(Build.VERSION_CODES.ECLAIR)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sekura process is running...")
            .setContentText("You can disable this notification in App info > Notifications.")
            .setSmallIcon(R.drawable.btn_star)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
        startForeground(1, notification)
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Sekura Service",
                NotificationManager.IMPORTANCE_MIN
            )
            serviceChannel.setShowBadge(false);
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}