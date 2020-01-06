package pl.redny.sekura.activity.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.tab1.*
import org.koin.android.ext.android.inject
import pl.redny.sekura.R
import pl.redny.sekura.activity.view.filePicker.FilePicker
import pl.redny.sekura.encryption.AESEncryptor
import pl.redny.sekura.encryption.DESEncryptor
import pl.redny.sekura.encryption.Encryptor
import pl.redny.sekura.util.ResourcesUtil

class Tab1 : Fragment() {

    private val filePicker: FilePicker by inject()

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
        button_picker_1.setOnClickListener { filePicker.openFilePicker(activity!!) }
        button_picker_2.setOnClickListener { filePicker.openSaveFile(activity!!, "file") }
        button_encrypt_action.setOnClickListener { onEncryptButton() }
        button_default_action.setOnClickListener { onDefaultButton() }
    }


    private fun toast(StringValue: Int) {
        Toast.makeText(
            activity,
            ResourcesUtil.getResource(context!!, StringValue),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onDefaultButton() {
        text_path_file.setText("")
        text_path_file_decrypted.setText("")
        password.setText("")
        radio_aes.isChecked = true
        radio_encrypt.isChecked = true
    }

    private fun onEncryptButton() {
        val mode: Boolean = radio_encrypt.isChecked
        val password = password.text.toString()

        if (checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            toast(R.string.control_no_permission)
            return
        }

        val inputStream = activity!!.contentResolver.openInputStream(Uri.parse(text_path_file.text.toString()))
        val outputStream =
            activity!!.contentResolver.openOutputStream(Uri.parse(text_path_file_decrypted.text.toString()))

        if (inputStream == null) {
            toast(R.string.encryption_input_error)
            return
        }

        if (outputStream == null) {
            toast(R.string.encryption_output_error)
            return
        }

        if (radio_aes.isChecked) {
            if (password.length < 16) {
                toast(R.string.encryption_incorrect_password_AES)
                return
            }
            encryptor = AESEncryptor()
        } else if (radio_des.isChecked) {
            if (password.length < 8) {
                toast(R.string.encryption_incorrect_password_DES)
                return
            }
            encryptor = DESEncryptor()
        }

        if (mode) {
            encryptor.encrypt(password, inputStream, outputStream)
        } else {
            encryptor.decrypt(password, inputStream, outputStream)
        }
        toast(R.string.encryption_successful)
    }
}