package pl.redny.sekura.activity.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.tab1.*
import pl.redny.sekura.R
import pl.redny.sekura.activity.ViewModel
import pl.redny.sekura.activity.view.filePicker.AndroidFilePicker
import pl.redny.sekura.activity.view.filePicker.FilePicker
import pl.redny.sekura.encryption.AESEncryptor
import pl.redny.sekura.encryption.DESEncryptor
import pl.redny.sekura.encryption.Encryptor

class Tab1 : Fragment() {

    private val filePicker: FilePicker = AndroidFilePicker()

    private val viewModel = ViewModel()

    private var encryptor: Encryptor = AESEncryptor()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.tab1, container, false)
    }

    override fun onStart() {
        super.onStart()
        button_picker_1.setOnClickListener { filePicker.openFilePicker( activity!!) }
        button_picker_2.setOnClickListener { filePicker.openSaveFile( activity!!,"file") }
        button_encrypt_action.setOnClickListener { onEncryptButton() }
    }


    private fun onEncryptButton() {
        val mode: Boolean = radio_encrypt.isChecked
        val password = password.text.toString()

        if (radio_aes.isChecked) {
            encryptor = AESEncryptor()
        } else if (radio_des.isChecked) {
            encryptor = DESEncryptor()
        }
        val inputStream = activity!!.contentResolver.openInputStream(Uri.parse(text_path_file.text.toString()))
        val outputStream = activity!!.contentResolver.openOutputStream(Uri.parse(text_path_file_decrypted.text.toString()))
        if (mode) {
            encryptor.encrypt(password, inputStream!!, outputStream!!)
        } else {
            encryptor.decrypt(password, inputStream!!, outputStream!!)
        }
    }
}