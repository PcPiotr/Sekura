package pl.redny.sekura.encryption

import java.io.InputStream
import java.io.OutputStream

interface Encryptor {
    fun encrypt(keyStr: String, input: InputStream, output: OutputStream)
    fun decrypt(keyStr: String, input: InputStream, output: OutputStream)

    class EncryptorException(message: String) : Exception(message)
}


