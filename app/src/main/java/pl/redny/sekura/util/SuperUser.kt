package pl.redny.sekura.util

import android.os.AsyncTask
import eu.chainfire.libsuperuser.Shell
import eu.chainfire.libsuperuser.Shell.Interactive
import eu.chainfire.libsuperuser.Shell.Threaded


class SuperUser : AsyncTask<Void, Void, Void>() {
    private val rootSession: Interactive? = null

    override fun doInBackground(vararg params: Void?): Void? {
        if (APIChecker.isRooted()) {
            Shell.SU.run("reboot")
        }
        return null
    }
}