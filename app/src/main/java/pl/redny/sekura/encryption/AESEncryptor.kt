package pl.redny.sekura.encryption

import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AESEncryptor : Encryptor {
    private val DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE = 1024
    private val ALGO_IMAGE_ENCRYPTOR = "AES/CBC/PKCS7Padding"
    private val ALGO_SECRET_KEY = "AES"
    private val IV_PARAMETER_SPEC = "C9JkDz3uaEQKpgct"
    private val SALT = "e9d71f5et7c92d6dc9212ffdad17b8bd4ge18f98".toByteArray(charset("UTF-8"))

    override fun encrypt(keyStr: String, input: InputStream, output: OutputStream) {
        logic(keyStr, input, output, true)
    }

    override fun decrypt(keyStr: String, input: InputStream, output: OutputStream) {
        logic(keyStr, input, output, false)
    }

    private fun logic(keyStr: String, input: InputStream, output: OutputStream, isEncrypt: Boolean) {
        var output = output
        try {
            val iv = IvParameterSpec(IV_PARAMETER_SPEC.toByteArray(charset("UTF-8")))

            val keySpec = PBEKeySpec("password".toCharArray(), SALT, 65536, 256)
            val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val key: ByteArray = keyFactory.generateSecret(keySpec).getEncoded()
            val secretKeySpec = SecretKeySpec(key, "AES")

            val cipher = Cipher.getInstance(ALGO_IMAGE_ENCRYPTOR)
            if (isEncrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv)
            }
            output = CipherOutputStream(output, cipher)

            val buffer = ByteArray(DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE)

            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } > 0)
                output.write(buffer, 0, bytesRead)
        } catch (exception: Exception) {
            throw Encryptor.EncryptorException(exception.message.toString())
        } finally {
            output.close()
        }

    }
}