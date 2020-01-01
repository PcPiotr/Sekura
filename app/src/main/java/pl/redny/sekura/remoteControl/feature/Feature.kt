package pl.redny.sekura.remoteControl.feature

import pl.redny.sekura.remoteControl.rule.Rule

interface Feature {
    var rules: MutableList<Rule>

    fun handle(parameters: HashMap<String, Any?>)

    fun canHandle(parameters: HashMap<String, Any?>): Boolean {
        var canHandle = false
        rules.forEach { rule ->
            if (rule.handle(parameters)) {
                canHandle = true
            }
        }
        return canHandle
    }
}