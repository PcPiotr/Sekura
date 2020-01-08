package pl.redny.sekura.remoteControl.feature

import android.content.Context
import android.net.Uri
import android.util.Log
import pl.redny.sekura.util.ResourcesUtil
import pl.redny.sekura.util.SuperUser
import java.io.File

class DeleteFile() : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        val uris = parameters["files"] as List<String>
        val context = parameters["context"] as Context
        for (uri in uris) {
            val file = File(uri)
            val isDeleted = file.delete()
            if (!isDeleted) {
                try {
                    SuperUser().execute("rm -f " + ResourcesUtil.getPath(context, Uri.parse(uri)))
                } catch (exception: IllegalArgumentException) {
                    Log.i("TEST", "File not exists")
                }
            }
        }
    }
}