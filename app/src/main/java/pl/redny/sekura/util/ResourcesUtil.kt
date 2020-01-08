package pl.redny.sekura.util

import android.content.Context
import android.content.res.Resources.NotFoundException

object ResourcesUtil {
    fun getResource(context: Context, number: Int): String? {
        return try {
            context.resources.getString(number)
        } catch (e: NotFoundException) {
            " "
        }
    }
}