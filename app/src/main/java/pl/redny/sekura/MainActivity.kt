package pl.redny.sekura

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import pl.redny.sekura.config.beans
import pl.redny.sekura.encryption.EncryptionService
import pl.redny.sekura.securityRaport.SecurityReport

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val securityReport: SecurityReport by inject()

    private val encryptionService: EncryptionService by inject()

    val contentMainBindingImpl = ViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            // your modules
            modules(beans)
        }

        setContentView(R.layout.activity_main)

        val helloTextView: TextView = findViewById(R.id.text_view_test_id)
        helloTextView.text = securityReport.getSecurityPatchDate()

        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, R.string.action_placeholder_toast, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_placeholder, null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            100
        )

        button_picker_1.setOnClickListener {

            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 2137)

        }
        button_picker_2.setOnClickListener {

            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TITLE, "newfile.txt")

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 2138)

        }


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        /*
        when (item.itemId) {
            R.id.nav_tbd -> {

            }
            R.id.nav_settings -> {

            }
            R.id.nav_about -> {

            }
        }
        */
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        if (requestCode == 2137) {
            contentMainBindingImpl.encryptedFilePath = data?.data
            text_path_file.text = Editable.Factory.getInstance().newEditable(data?.data.toString())

        } else if (requestCode == 2138) {
            contentMainBindingImpl.decryptedFilePath = data?.data
            text_path_file_decrypted.text = Editable.Factory.getInstance().newEditable(data?.data.toString())
        }
    }
}
