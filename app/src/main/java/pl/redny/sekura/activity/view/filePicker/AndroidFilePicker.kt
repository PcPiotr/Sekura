package pl.redny.sekura.activity.view.filePicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import pl.redny.sekura.R
import pl.redny.sekura.util.ResourcesUtil

class AndroidFilePicker : FilePicker {
    override fun openFilePicker(appCompatActivity: AppCompatActivity) {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        appCompatActivity.startActivityForResult(
            Intent.createChooser(
                intent, ResourcesUtil.getResource(
                    appCompatActivity,
                    R.string.action_file_pick
                )
            ), 2137
        )

    }

    override fun openSaveFile(appCompatActivity: AppCompatActivity, defaultSaveFile: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_TITLE, "newfile.txt")

        appCompatActivity.startActivityForResult(
            Intent.createChooser(
                intent, ResourcesUtil.getResource(
                    appCompatActivity,
                    R.string.action_file_pick
                )
            ), 2138
        )
    }
}