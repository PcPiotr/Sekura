package pl.redny.sekura.remoteControl.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import pl.redny.sekura.remoteControl.rule.BroadcastRule

class SmsBroadcastReceiver(private val smsBroadcastRules: MutableList<BroadcastRule>) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            var smsSender = ""
            var smsBody = ""
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsSender = smsMessage.displayOriginatingAddress
                smsBody += smsMessage.messageBody
            }

            val parameters: HashMap<String, Any> = hashMapOf("smsSender" to smsSender, "smsBody" to smsBody);
            smsBroadcastRules.forEach { rule ->
                rule.handle(parameters)
            }
        }
    }

    fun addRule(rule: BroadcastRule) {
        smsBroadcastRules.add(rule)
    }

    fun removeRule(rule: BroadcastRule) {
        smsBroadcastRules.remove(rule)
    }

    fun removeAt(value: Int) {
        smsBroadcastRules.removeAt(value)
    }
}