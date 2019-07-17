package pl.redny.sekura.util

import android.os.Build
import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader

class APIChecker {
    companion object {
        private val rootPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )

        fun isMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        fun isNougat() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        fun isOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        fun isPie() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

        fun isRooted(): Boolean = run {
            if (Build.TAGS != null && Build.TAGS.contains("test-keys")) {
                return true
            }

            rootPaths.forEach {
                if (File(it).exists()) {
                    return true
                }
            }

            var process: Process? = null
            return try {
                process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
                val input = BufferedReader(InputStreamReader(process!!.inputStream))
                input.readLine() != null
            } catch (t: Throwable) {
                false
            } finally {
                process?.destroy()
            }
        }
    }
}