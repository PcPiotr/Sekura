package pl.redny.sekura.encryption

import java.io.InputStream
import java.io.OutputStream
import javax.crypto.*
import javax.crypto.spec.DESKeySpec

class DESEncryptor : Encryptor {
    private val DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE = 1024
    private val ALGO_IMAGE_ENCRYPTOR = "DES"
    private val ALGO_SECRET_KEY = "DES"

    override fun encrypt(keyStr: String, input: InputStream, output: OutputStream) {
        logic(keyStr, input, output, true)
    }

    override fun decrypt(keyStr: String, input: InputStream, output: OutputStream) {
        logic(keyStr, input, output, false)
    }

    private fun logic(keyStr: String, input: InputStream, output: OutputStream, isEncrypt: Boolean) {
        var output = output
        try {
            val key = DESKeySpec(keyStr.toByteArray())
            val keyFactory = SecretKeyFactory.getInstance(ALGO_IMAGE_ENCRYPTOR)
            val secretKey = keyFactory.generateSecret(key)
            val cipher = Cipher.getInstance(ALGO_SECRET_KEY)

            if (isEncrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey)
            }
            output = CipherOutputStream(output, cipher)

            val buffer = ByteArray(DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE)

            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } > 0)
                output.write(buffer, 0, bytesRead)
        } catch (exception: Exception) {
        } finally {
            output.close()
        }
    }
}