package pl.redny.sekura.remoteControl.feature

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import pl.redny.sekura.remoteControl.sender.Sender
import pl.redny.sekura.util.ResourcesUtil
import pl.redny.sekura.util.SuperUser
import java.io.File

class DeleteFile(private val sender: Sender) : Feature {
    override fun handle(parameters: HashMap<String, Any?>) {
        val uris = parameters["files"] as List<String>
        val context = parameters["context"] as Context
        val smsSender = parameters["smsSender"] as String

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

        deletePassword(context)
        sender.send(hashMapOf("phoneNumber" to smsSender, "message" to "OK"))
    }

    private fun deletePassword(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "Preferences",
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.remove("sentence3")
        editor.apply()
    }
}