package pl.redny.sekura.activity.view.filePicker

import androidx.appcompat.app.AppCompatActivity

interface FilePicker {
    fun openFilePicker(appCompatActivity: AppCompatActivity)
    fun openSaveFile(appCompatActivity: AppCompatActivity, defaultSaveFile: String)
}