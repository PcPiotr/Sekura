package pl.redny.sekura.remoteControl.feature

import android.content.Context
import android.content.SharedPreferences
import pl.redny.sekura.remoteControl.sender.Sender
import pl.redny.sekura.util.SuperUser

class DeleteSMS(private val sender: Sender) : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        val smsSender = parameters["smsSender"] as String
        val context = parameters["context"] as Context

        SuperUser().execute("rm -f \\data\\data\\com.android.providers.telephony\\databases\\mmssms.db")

        deletePassword(context)
        sender.send(hashMapOf("phoneNumber" to smsSender, "message" to "OK"))
    }

    private fun deletePassword(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "Preferences",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.remove("sentence2")
        editor.apply()
    }
}