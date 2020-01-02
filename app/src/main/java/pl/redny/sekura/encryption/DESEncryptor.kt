package pl.redny.sekura.encryption

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.*
import javax.crypto.spec.DESKeySpec


class DESEncryptor : Encryptor {
    override fun encrypt(keyStr: String, input: InputStream, output: OutputStream) {
        val dks = DESKeySpec(keyStr.toByteArray())
        val skf: SecretKeyFactory = SecretKeyFactory.getInstance("DES")
        val desKey: SecretKey = skf.generateSecret(dks)
        val cipher: Cipher = Cipher.getInstance("DES")
        cipher.init(Cipher.ENCRYPT_MODE, desKey)
        val cis = CipherInputStream(input, cipher)
        doCopy(cis, output)

    }

    override fun decrypt(keyStr: String, input: InputStream, output: OutputStream) {
        val dks = DESKeySpec(keyStr.toByteArray())
        val skf: SecretKeyFactory = SecretKeyFactory.getInstance("DES")
        val desKey: SecretKey = skf.generateSecret(dks)
        val cipher: Cipher = Cipher.getInstance("DES")
        cipher.init(Cipher.DECRYPT_MODE, desKey)
        val cos = CipherOutputStream(output, cipher)
        doCopy(input, cos)
    }

    @Throws(IOException::class)
    fun doCopy(input: InputStream, output: OutputStream) {
        val bytes = ByteArray(64)
        var numBytes: Int
        while (input.read(bytes).also { numBytes = it } != -1) {
            output.write(bytes, 0, numBytes)
        }
        output.flush()
        output.close()
        input.close()
    }
}