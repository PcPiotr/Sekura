package pl.redny.sekura

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import pl.redny.sekura.config.myModule
import pl.redny.sekura.securityRaport.SecurityReport

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val securityReport: SecurityReport by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            // your modules
            modules(myModule)
        }

        setContentView(R.layout.activity_main)

        val helloTextView : TextView = findViewById(R.id.text_view_test_id)
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
}
