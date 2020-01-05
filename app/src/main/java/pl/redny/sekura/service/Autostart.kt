package pl.redny.sekura.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pl.redny.sekura.activity.MainActivity


class Autostart : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action
        if (Intent.ACTION_BOOT_COMPLETED == action) {
            startServiceDirectly(context!!)
        }
    }

    private fun startServiceDirectly(context: Context) {
        val app = Intent(context, AutostartActivity::class.java)
        app.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(app)
    }
}