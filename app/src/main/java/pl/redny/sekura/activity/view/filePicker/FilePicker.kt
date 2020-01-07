package pl.redny.sekura.activity.view.filePicker

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

interface FilePicker {
    fun openFilePicker(activity: Activity, code: Int)
    fun openSaveFile(activity: Activity, defaultSaveFile: String, code: Int)
}