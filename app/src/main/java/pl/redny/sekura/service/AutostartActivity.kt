package pl.redny.sekura.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import pl.redny.sekura.remoteControl.feature.DeleteFile
import pl.redny.sekura.remoteControl.feature.DeleteSMS
import pl.redny.sekura.remoteControl.feature.SharePhoneLocation
import pl.redny.sekura.remoteControl.sender.SmsSender
import pl.redny.sekura.util.APIChecker

class AutostartActivity : Activity() {

    private val deleteFile: DeleteFile = DeleteFile()

    private val deleteSMS: DeleteSMS = DeleteSMS()

    private val sharePhoneLocation: SharePhoneLocation = SharePhoneLocation(SmsSender())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val i = Intent(this, ForegroundService::class.java)
        if (APIChecker.isOreo()) {
            startForegroundService(i)
        } else {
            startService(i)
        }

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
                        "files" to sharedPreferences.getStringSet("fileSet", setOf())!!.toList()
                    )
                    deleteFile.handle(parameters)
                }
            }
        }

        moveTaskToBack(true)
    }

    override fun onPostResume() {
        super.onPostResume()
        finish()
    }
}