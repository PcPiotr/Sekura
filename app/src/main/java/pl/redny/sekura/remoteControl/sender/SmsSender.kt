package pl.redny.sekura.remoteControl.sender

import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import androidx.annotation.RequiresApi

class SmsSender : Sender {

    /**
     * TAG for logcat logging.
     */
    private val TAG = "SmsSender"

    @RequiresApi(Build.VERSION_CODES.DONUT)
    override fun send(parameters: HashMap<String, Any>) {
        val number = parameters["phoneNumber"] as String
        val message = parameters["message"] as String

        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number, null, message, null, null)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending SMS: " + e.message)
        }
    }
}