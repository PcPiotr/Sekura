package pl.redny.sekura.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import pl.redny.sekura.util.APIChecker


class AutostartActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val i = Intent(this, ForegroundService::class.java)
        if (APIChecker.isOreo()) {
            startForegroundService(i)
        } else {
            startService(i)
        }

        moveTaskToBack(true)
    }

    override fun onPostResume() {
        super.onPostResume()
        finish()
    }
}