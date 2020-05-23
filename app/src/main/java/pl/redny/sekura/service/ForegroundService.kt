package pl.redny.sekura.service

import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.koin.android.ext.android.inject
import pl.redny.sekura.activity.MainActivity
import pl.redny.sekura.activity.ViewModel
import pl.redny.sekura.remoteControl.feature.DeleteFile
import pl.redny.sekura.remoteControl.feature.DeleteSMS
import pl.redny.sekura.remoteControl.receiver.LocationReceiver
import pl.redny.sekura.remoteControl.receiver.SmsBroadcastReceiver
import pl.redny.sekura.util.APIChecker
import pl.redny.sekura.util.ResourcesUtil


class ForegroundService : Service() {
    val CHANNEL_ID = "ForegroundSekuraServiceChannel"

    private var locationReceiver: LocationReceiver? = null
    private val intentFilter = IntentFilter()
    private val broadcastReceiver: SmsBroadcastReceiver by inject()
    private val viewModel: ViewModel by inject()
    private val deleteFile: DeleteFile by inject()
    private val deleteSMS: DeleteSMS by inject()

    override fun onCreate() {
        super.onCreate()
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(broadcastReceiver, intentFilter)
        locationReceiver = LocationReceiver(getSystemService(Context.LOCATION_SERVICE) as LocationManager, viewModel)
        if (APIChecker.isMarshmallow()) {
            if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationEnabler()
            }
        } else {
            locationEnabler()
        }
    }

    @SuppressLint("MissingPermission")
    private fun locationEnabler() {
        if ((getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationReceiver!!.locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0,
                0f,
                locationReceiver!!
            )
        }
        if ((getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationReceiver!!.locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationReceiver!!
            )
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Binder()
    }

    fun autostartErasure() {
        val simState = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simState
        if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            val sharedPreferences: SharedPreferences = getSharedPreferences(
                "Preferences",
                Context.MODE_PRIVATE
            )

            if (sharedPreferences.getBoolean("simFeature", false)) {
                if (sharedPreferences.getBoolean("feature2", false)) {
                    deleteSMS.handle(hashMapOf())
                }

                if (sharedPreferences.getBoolean("feature3", false)) {
                    val parameters: HashMap<String, Any?> = hashMapOf(
                        "files" to sharedPreferences.getStringSet("fileSet", setOf())!!.toList(),
                        "context" to this,
                        "smsSender" to sharedPreferences.getString("phoneNumber", "")
                        )
                    deleteFile.handle(parameters)
                }
            }
        }
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
            .setContentTitle(ResourcesUtil.getResource(this, pl.redny.sekura.R.string.notification_service_title))
            .setContentText(ResourcesUtil.getResource(this, pl.redny.sekura.R.string.notification_service_content))
            .setSmallIcon(R.drawable.btn_star)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
        startForeground(1, notification)
        return START_STICKY
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
            getSystemService(NotificationManager::class.java)!!.createNotificationChannel(serviceChannel)
        }
    }
}