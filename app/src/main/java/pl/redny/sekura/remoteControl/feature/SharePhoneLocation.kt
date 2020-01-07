package pl.redny.sekura.remoteControl.feature

import pl.redny.sekura.activity.ViewModel
import pl.redny.sekura.remoteControl.sender.Sender

class SharePhoneLocation(private val sender: Sender) : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        val smsSender = parameters["smsSender"] as String
        val viewModel = parameters["viewModel"] as ViewModel
        val message: String = "" + viewModel.latitude + "\n" + viewModel.longitude
        sender.send(hashMapOf("phoneNumber" to smsSender, "message" to message))
    }
}