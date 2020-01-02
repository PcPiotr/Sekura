
package pl.redny.sekura.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab1.*
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import pl.redny.sekura.R
import pl.redny.sekura.activity.view.filePicker.FilePicker
import pl.redny.sekura.config.mainActivityModules
import pl.redny.sekura.encryption.EncryptionService
import pl.redny.sekura.encryption.Encryptor
import pl.redny.sekura.encryption.AESEncryptor
import pl.redny.sekura.encryption.DESEncryptor
import pl.redny.sekura.remoteControl.receiver.SmsBroadcastReceiver

class MainActivity : AppCompatActivity() {

    private val encryptionService: EncryptionService by inject()

    private val filePicker: FilePicker by inject()

    private val viewModel = ViewModel()

    private val broadcastReceiver = SmsBroadcastReceiver()

    private var encryptor: Encryptor = AESEncryptor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            modules(mainActivityModules)
        }

        setContentView(R.layout.activity_main)

        var adapterViewPager = MyPageAdapter(supportFragmentManager)
        view_pager.adapter = adapterViewPager

        val tabs: TabLayout = findViewById(R.id.view_pager_tab)
        tabs.setupWithViewPager(view_pager)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            100
        )

        button_picker_1.setOnClickListener { filePicker.openFilePicker(this) }
        button_picker_2.setOnClickListener { filePicker.openSaveFile(this, "file") }
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
        val inputStream = contentResolver.openInputStream(Uri.parse(text_path_file.text.toString()))
        val outputStream = contentResolver.openOutputStream(Uri.parse(text_path_file_decrypted.text.toString()))
        if (mode) {
            encryptor.encrypt(password, inputStream!!, outputStream!!)
        } else {
            encryptor.decrypt(password, inputStream!!, outputStream!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            2137 -> {
                viewModel.encryptedFilePath = data?.data
                text_path_file.text = Editable.Factory.getInstance().newEditable(data?.data.toString())
                //text_path_file.text = Editable.Factory.getInstance().newEditable(ResourcesUtil.getPath(this, Uri.parse(data?.data.toString())))
            }
            2138 -> {
                viewModel.decryptedFilePath = data?.data
                text_path_file_decrypted.text = Editable.Factory.getInstance().newEditable(data?.data.toString())
            }
        }
    }
}
