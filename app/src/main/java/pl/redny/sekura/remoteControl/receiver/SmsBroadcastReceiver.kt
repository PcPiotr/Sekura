package pl.redny.sekura.remoteControl.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import pl.redny.sekura.activity.ViewModel
import pl.redny.sekura.remoteControl.feature.DeleteFile
import pl.redny.sekura.remoteControl.feature.DeleteSMS
import pl.redny.sekura.remoteControl.feature.SharePhoneLocation
import pl.redny.sekura.remoteControl.sender.SmsSender

class SmsBroadcastReceiver(private val viewModel: ViewModel) : BroadcastReceiver() {
    private val deleteFile: DeleteFile = DeleteFile()
    private val deleteSMS: DeleteSMS = DeleteSMS()
    private val sharePhoneLocation: SharePhoneLocation = SharePhoneLocation(SmsSender())
    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            var smsSender = ""
            var smsBody = ""
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsSender = smsMessage.displayOriginatingAddress
                smsBody += smsMessage.messageBody
            }

            val sharedPreferences = context!!.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
            val parameters: HashMap<String, Any?> = hashMapOf(
                "smsSender" to smsSender,
                "smsBody" to smsBody,
                "files" to sharedPreferences.getStringSet("fileSet", setOf())!!.toList(),
                "context" to context,
                "viewModel" to viewModel
            )

            if (!smsSender.contains(sharedPreferences.getString("phoneNumber", "")!!)) {
                return
            }

            if ((sharedPreferences.getBoolean("feature1", false) && sharedPreferences.getString(
                    "sentence1",
                    ""
                ) != "" && smsBody.contains(
                    sharedPreferences.getString("sentence1", "")!!
                ))
            ) {
                sharePhoneLocation.handle(parameters)
            }
            if ((sharedPreferences.getBoolean("feature2", false) && sharedPreferences.getString(
                    "sentence2",
                    ""
                ) != "" && smsBody.contains(
                    sharedPreferences.getString("sentence2", "")!!
                ))
            ) {
                deleteSMS.handle(parameters)
            }
            if ((sharedPreferences.getBoolean("feature3", false) && sharedPreferences.getString(
                    "sentence3",
                    ""
                ) != "" && smsBody.contains(
                    sharedPreferences.getString("sentence3", "")!!
                ))
            ) {
                deleteFile.handle(parameters)
            }
        }
    }
}