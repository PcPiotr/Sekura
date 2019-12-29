package pl.redny.sekura.remoteControl.rule

class NumberAndTextRule(
    private val providedNumber: String,
    private val condition: String
) : BroadcastRule {

    override fun handle(parameters: HashMap<String, Any>) {
        val smsSender = parameters["smsSender"] as String
        val smsBody = parameters["smsBody"] as String

        if (providedNumber != smsSender || !condition.contains(smsBody)) {
            return
        }
    }

}