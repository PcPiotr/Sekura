package pl.redny.sekura.remoteControl.feature

import eu.chainfire.libsuperuser.Shell

class DeleteSMS() : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        Shell.SU.run("rm -f \\data\\data\\com.android.providers.telephony\\databases\\mmssms.db");
    }
}