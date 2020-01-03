package pl.redny.sekura

import android.app.Application
import org.koin.core.context.startKoin
import pl.redny.sekura.config.mainActivityModules

class SekuraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(mainActivityModules)
        }
    }

}