package pl.redny.sekura.view.filePicker

import android.app.Activity

interface FilePicker {
    fun openFilePicker(activity: Activity, code: Int)
    fun openSaveFile(activity: Activity, defaultSaveFile: String, code: Int)
}