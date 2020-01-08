package pl.redny.sekura.activity.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.tab1.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import pl.redny.sekura.R
import pl.redny.sekura.encryption.AESEncryptor
import pl.redny.sekura.encryption.DESEncryptor
import pl.redny.sekura.encryption.Encryptor
import pl.redny.sekura.util.ResourcesUtil
import pl.redny.sekura.view.filePicker.FilePicker
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.OutputStream


class Tab1 : Fragment() {
    /**
     * TAG for logcat logging.
     */
    private val TAG = "APIChecker"

    val CHANNEL_ID = "SekuraEncryptionChannel"

    private val filePicker: FilePicker by inject()

    private var encryptor: Encryptor = AESEncryptor()

    private var notificationManager: NotificationManager? = null

    private var notificationBuilder: NotificationCompat.Builder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createNotificationChannel()
        return inflater.inflate(R.layout.tab1, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        button_picker_1.setOnClickListener { filePicker.openFilePicker(activity!!, 2137) }
        button_picker_2.setOnClickListener { filePicker.openSaveFile(activity!!, "file", 2138) }
        button_encrypt_action.setOnClickListener { onEncryptButton() }
        button_default_action.setOnClickListener { onDefaultButton() }
        notificationManager = activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationBuilder = NotificationCompat.Builder(activity)
        notificationBuilder!!.setContentTitle("Encryption")
            .setSmallIcon(R.drawable.ic_menu_send)
            .setProgress(100, 0, true)
            .setChannelId(CHANNEL_ID)
            .priority = NotificationManager.IMPORTANCE_DEFAULT
    }

    private fun toast(stringValue: Int) {
        Toast.makeText(
            activity,
            ResourcesUtil.getResource(context!!, stringValue),
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
            Log.w(TAG, "Cannot get permission:" + Manifest.permission.WRITE_EXTERNAL_STORAGE)
            toast(R.string.control_no_permission)
            return
        }

        val inputStream: InputStream?
        val outputStream: OutputStream?
        try {
            inputStream = activity!!.contentResolver.openInputStream(Uri.parse(text_path_file.text.toString()))
        } catch (exception: FileNotFoundException) {
            Log.w(TAG, "Cannot get file:" + text_path_file.text.toString())
            toast(R.string.encryption_input_error)
            return
        }
        try {
            outputStream =
                activity!!.contentResolver.openOutputStream(Uri.parse(text_path_file_decrypted.text.toString()))
        } catch (exception: FileNotFoundException) {
            Log.w(TAG, "Cannot get file:" + text_path_file_decrypted.text.toString())
            toast(R.string.encryption_output_error)
            return
        }

        encryptor = when {
            radio_aes.isChecked -> {
                AESEncryptor()
            }
            radio_des.isChecked -> {
                DESEncryptor()
            }
            else -> {
                Log.e(TAG, "Encryptor not found")
                return
            }
        }

        try {
            if (mode) {
                GlobalScope.async {
                    notificationBuilder!!.setContentText("Encryption process in progress...")
                    notificationManager!!.notify(420, notificationBuilder!!.build())
                    encryptor.encrypt(password, inputStream!!, outputStream!!)
                    notificationBuilder!!.setContentText("Encryption completed.")
                        .setProgress(0, 0, false)
                    notificationManager!!.notify(420, notificationBuilder!!.build())
                    Log.i(TAG, "Encryption process successful")
                }

            } else {
                GlobalScope.async {
                    notificationBuilder!!.setContentText("Decryption process in progress...")
                    notificationManager!!.notify(420, notificationBuilder!!.build())
                    encryptor.decrypt(password, inputStream!!, outputStream!!)
                    notificationBuilder!!.setContentText("Decryption completed.")
                        .setProgress(0, 0, false)
                    notificationManager!!.notify(420, notificationBuilder!!.build())
                    Log.i(TAG, "Decryption process successful")
                }
            }
        } catch (exception: Encryptor.EncryptorException) {
            Log.e(TAG, "Error during encryption: " + exception.message)
            toast(R.string.encryption_unsuccessful)
        }

        toast(R.string.encryption_successful)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Encryption progess",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            serviceChannel.setShowBadge(false);
            activity!!.getSystemService(NotificationManager::class.java)!!.createNotificationChannel(serviceChannel)
        }
    }
}