package pl.redny.sekura.remoteControl.feature

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import pl.redny.sekura.remoteControl.rule.Rule
import java.io.File


class DeleteFile(override var rules: MutableList<Rule>) : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        if (!canHandle(parameters)) {
            return
        }
        val uris = parameters["files"] as List<String>;
        val context = parameters["context"] as Context
        for (uri in uris) {
            val uriDelete = Uri.parse(uri)
            val contentResolver: ContentResolver = context.contentResolver
            contentResolver.delete(uriDelete, null, null)
        }
    }
}