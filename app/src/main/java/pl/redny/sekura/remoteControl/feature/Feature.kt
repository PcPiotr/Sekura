package pl.redny.sekura.remoteControl.feature

interface Feature {
    fun handle(parameters: HashMap<String, Any?>)
}