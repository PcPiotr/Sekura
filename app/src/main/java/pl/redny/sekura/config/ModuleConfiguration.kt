package pl.redny.sekura.config

import org.koin.dsl.bind
import org.koin.dsl.module
import pl.redny.sekura.activity.view.filePicker.AndroidFilePicker
import pl.redny.sekura.activity.view.filePicker.FilePicker
import pl.redny.sekura.encryption.EncryptionService
import pl.redny.sekura.encryption.Encryptor
import pl.redny.sekura.encryption.AESEncryptor
import pl.redny.sekura.encryption.DESEncryptor
import pl.redny.sekura.remoteControl.feature.DeleteFile
import pl.redny.sekura.remoteControl.feature.DeleteSMS
import pl.redny.sekura.remoteControl.feature.SharePhoneLocation
import pl.redny.sekura.remoteControl.sender.SmsSender

class ModuleConfiguration

val mainActivityModules = module {
    single { ModuleConfiguration() }
    single { EncryptionService(listOf(AESEncryptor())) }
    single { AndroidFilePicker() } bind FilePicker::class
    single { AESEncryptor() } bind Encryptor::class
    single { DESEncryptor() }
    single { DeleteFile() }
    single { DeleteSMS() }
    single { SharePhoneLocation(SmsSender()) }
    single { pl.redny.sekura.activity.ViewModel() }
}
