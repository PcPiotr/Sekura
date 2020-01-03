package pl.redny.sekura.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.tabs.TabLayout
import com.guardanis.applock.AppLock
import com.guardanis.applock.activities.LockCreationActivity
import com.guardanis.applock.activities.UnlockActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab1.*
import org.koin.android.ext.android.inject
import pl.redny.sekura.R
import pl.redny.sekura.activity.view.filePicker.FilePicker
import pl.redny.sekura.encryption.AESEncryptor
import pl.redny.sekura.encryption.EncryptionService
import pl.redny.sekura.encryption.Encryptor
import pl.redny.sekura.remoteControl.receiver.SmsBroadcastReceiver


class MainActivity : AppCompatActivity() {

    private val encryptionService: EncryptionService by inject()

    private val filePicker: FilePicker by inject()

    private val viewModel = ViewModel()

    private val broadcastReceiver = SmsBroadcastReceiver()

    private var encryptor: Encryptor = AESEncryptor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val adapterViewPager = MyPageAdapter(supportFragmentManager, this)
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

        if (AppLock.isEnrolled(this)) {
            val intent = Intent(this, UnlockActivity::class.java)
            startActivityForResult(intent, AppLock.REQUEST_CODE_UNLOCK)
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
            }
            2138 -> {
                viewModel.decryptedFilePath = data?.data
                text_path_file_decrypted.text = Editable.Factory.getInstance().newEditable(data?.data.toString())
            }
        }
    }
}
