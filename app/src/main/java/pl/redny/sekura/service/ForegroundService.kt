package pl.redny.sekura.service

import android.R
import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import pl.redny.sekura.activity.MainActivity
import pl.redny.sekura.remoteControl.receiver.SmsBroadcastReceiver


class ForegroundService : Service() {
    val CHANNEL_ID = "ForegroundSekuraServiceChannel"
    private val broadcastReceiver = SmsBroadcastReceiver()
    private val intentFilter = IntentFilter()

    override fun onCreate() {
        super.onCreate()
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Binder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Sekura")
            .setContentText("")
            .setSmallIcon(R.drawable.sym_def_app_icon)
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
                "Foreground Sekura Service Channel",
                NotificationManager.IMPORTANCE_MIN
            )
            serviceChannel.setShowBadge(false);
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}