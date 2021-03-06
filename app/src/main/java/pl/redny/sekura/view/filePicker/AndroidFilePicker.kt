package pl.redny.sekura.view.filePicker

import android.app.Activity
import android.content.Intent
import pl.redny.sekura.R
import pl.redny.sekura.util.ResourcesUtil

class AndroidFilePicker : FilePicker {
    override fun openFilePicker(activity: Activity, code: Int) {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        activity.startActivityForResult(
            Intent.createChooser(
                intent, ResourcesUtil.getResource(
                    activity,
                    R.string.encryption_file_pick
                )
            ), code
        )

    }

    override fun openSaveFile(activity: Activity, defaultSaveFile: String, code: Int) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType("*/*")
            .putExtra(Intent.EXTRA_TITLE, defaultSaveFile)

        activity.startActivityForResult(
            Intent.createChooser(
                intent, ResourcesUtil.getResource(
                    activity,
                    R.string.encryption_file_pick
                )
            ), code
        )
    }
}