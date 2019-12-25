package pl.redny.sekura.config

import org.koin.dsl.module
import pl.redny.sekura.securityRaport.SecurityReport

class ModuleConfiguration(val securityReport: SecurityReport)

// just declare it
val myModule = module {
    single { ModuleConfiguration(get()) }
    single { SecurityReport() }
}