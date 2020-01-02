package pl.redny.sekura.remoteControl.rule

class NumberAndTextRule(
    private val providedNumber: String,
    private val condition: String
) : Rule {

    override fun handle(parameters: HashMap<String, Any?>): Boolean {
        val smsSender = parameters["smsSender"] as String
        val smsBody = parameters["smsBody"] as String

        return (providedNumber == smsSender && condition.contains(smsBody))
    }

}