package pl.redny.sekura.remoteControl.rule

interface BroadcastRule {
    fun handle(parameters: HashMap<String, Any>)
}