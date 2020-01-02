package pl.redny.sekura.remoteControl.rule

class IsEnabled : Rule {
    override fun handle(parameters: HashMap<String, Any?>): Boolean {
        return true
    }
}