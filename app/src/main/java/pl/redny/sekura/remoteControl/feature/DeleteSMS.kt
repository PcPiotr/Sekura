package pl.redny.sekura.remoteControl.feature

import pl.redny.sekura.util.SuperUser

class DeleteSMS() : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        SuperUser().execute("rm -f \\data\\data\\com.android.providers.telephony\\databases\\mmssms.db")
    }
}