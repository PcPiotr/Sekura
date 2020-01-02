package pl.redny.sekura.remoteControl.rule

class TextRule(
    private val condition: String
) : Rule {

    override fun handle(parameters: HashMap<String, Any?>): Boolean {
        val smsBody = parameters["smsBody"] as String

        return (smsBody.contains(condition))
    }

}