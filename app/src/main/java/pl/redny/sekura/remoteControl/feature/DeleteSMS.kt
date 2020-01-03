package pl.redny.sekura.remoteControl.feature

import eu.chainfire.libsuperuser.Shell
import pl.redny.sekura.remoteControl.rule.Rule

class DeleteSMS(override var rules: MutableList<Rule>) : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        if (!canHandle(parameters)) {
            return
        }

        Shell.SU.run("rm -f \\data\\data\\com.android.providers.telephony\\databases\\mmssms.db");
    }
}