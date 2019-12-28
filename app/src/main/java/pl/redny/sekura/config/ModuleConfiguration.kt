package pl.redny.sekura.config

import org.koin.dsl.module
import pl.redny.sekura.encryption.EncryptionService
import pl.redny.sekura.encryption.TestEncryptor
import pl.redny.sekura.securityRaport.SecurityReport

class ModuleConfiguration(val securityReport: SecurityReport)

// just declare it
val beans = module {
    single { ModuleConfiguration(get()) }
    single { SecurityReport() }
    single { EncryptionService(listOf(TestEncryptor())) }
}