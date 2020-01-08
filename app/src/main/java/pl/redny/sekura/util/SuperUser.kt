package pl.redny.sekura.util

import android.os.AsyncTask
import eu.chainfire.libsuperuser.Shell

class SuperUser : AsyncTask<String, Void, Void>() {

    override fun doInBackground(vararg params: String?): Void? {
        if (APIChecker.isRooted()) {
            Shell.SU.run(params[0] as String)
        }
        return null
    }
}