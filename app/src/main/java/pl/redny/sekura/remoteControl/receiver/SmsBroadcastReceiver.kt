package pl.redny.sekura.remoteControl.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import pl.redny.sekura.remoteControl.feature.DeleteFile
import pl.redny.sekura.remoteControl.feature.SharePhoneLocation
import pl.redny.sekura.remoteControl.rule.Rule
import pl.redny.sekura.remoteControl.rule.TextRule
import pl.redny.sekura.remoteControl.sender.SmsSender

class SmsBroadcastReceiver() : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            var smsSender = ""
            var smsBody = ""
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsSender = smsMessage.displayOriginatingAddress
                smsBody += smsMessage.messageBody
            }

            val parameters: HashMap<String, Any?> = hashMapOf(
                "smsSender" to smsSender,
                "smsBody" to smsBody,
                "files" to listOf("content://media/external/images/media/251"),
                "context" to context
            )

            //WIP af
            var textRule1 = TextRule("Jezus")
            var deleteFileTest = DeleteFile(mutableListOf(textRule1))
            deleteFileTest.handle(parameters);

            var textRule2 = TextRule("Howard")
            var sharePhoneLocation = SharePhoneLocation(mutableListOf(textRule2), SmsSender())
            sharePhoneLocation.handle(parameters)
        }
    }

//    fun addRule(rule: Rule) {
//        smsBroadcastRules.add(rule)
//    }
//
//    fun removeRule(rule: Rule) {
//        smsBroadcastRules.remove(rule)
//    }
//
//    fun removeAt(value: Int) {
//        smsBroadcastRules.removeAt(value)
//    }
}