package pl.redny.sekura.remoteControl.rule

interface Rule {
    fun handle(parameters: HashMap<String, Any?>) : Boolean
}