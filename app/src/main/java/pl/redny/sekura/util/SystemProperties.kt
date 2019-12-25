package pl.redny.sekura.util

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Class responsible for operating on system properties.
 */
object SystemProperties {
    /**
     * Path to getprop file.
     */
    private const val GETPROP_EXECUTABLE_PATH = "/system/bin/getprop"
    /**
     * TAG for logcat logging.
     */
    private const val TAG = "SystemProperties"

    /**
     * Method responsible for getting prop from getprop file.
     *
     * @param propName [String] name of desired prop.
     * @return [String] returns desired prop.
     * Returns empty [String] if prop is not found.
     */
    fun getPropFromGetProp(propName: String): String {
        var process: Process? = null
        var bufferedReader: BufferedReader? = null

        try {
            process = ProcessBuilder().command(GETPROP_EXECUTABLE_PATH, propName).redirectErrorStream(true).start()
            bufferedReader = BufferedReader(InputStreamReader(process!!.inputStream))
            var line: String? = bufferedReader.readLine()
            if (line == null) {
                line = "" //prop not set
            }
            Log.i(TAG, "read System Property: $propName=$line")
            return line
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read System Property $propName", e)
            return ""
        } finally {
            bufferedReader?.close()
            process?.destroy()
        }
    }
}