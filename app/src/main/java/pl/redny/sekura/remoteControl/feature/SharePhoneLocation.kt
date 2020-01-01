package pl.redny.sekura.remoteControl.feature

import pl.redny.sekura.remoteControl.rule.Rule
import pl.redny.sekura.remoteControl.sender.Sender

class SharePhoneLocation(override var rules: MutableList<Rule>, private val sender: Sender) : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        if (!canHandle(parameters)) {
            return
        }

        val smsSender = parameters["smsSender"] as String
        sender.send(hashMapOf("phoneNumber" to smsSender, "message" to "test"))
    }
}