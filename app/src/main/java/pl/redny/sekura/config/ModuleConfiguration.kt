package pl.redny.sekura.config

import org.koin.dsl.bind
import org.koin.dsl.module
import pl.redny.sekura.activity.view.filePicker.AndroidFilePicker
import pl.redny.sekura.activity.view.filePicker.FilePicker
import pl.redny.sekura.encryption.EncryptionService
import pl.redny.sekura.encryption.enryptorImpl.TestEncryptor

class ModuleConfiguration

val mainActivityModule = module {
    single { ModuleConfiguration() }
    single { EncryptionService(listOf(TestEncryptor())) }
    single { AndroidFilePicker() } bind FilePicker::class

}