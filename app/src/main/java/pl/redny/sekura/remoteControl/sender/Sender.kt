package pl.redny.sekura.remoteControl.sender

interface Sender {
    fun send(parameters: HashMap<String, Any>)
}