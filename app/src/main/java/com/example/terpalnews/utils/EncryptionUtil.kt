package com.example.terpalnews.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {

    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val SECRET_KEY = "baciro"

    // Method to generate a new random IV
    private fun generateIv(): ByteArray {
        val iv = ByteArray(16) // 128-bit IV for AES
        SecureRandom().nextBytes(iv)
        return iv
    }

    // Encrypt password with a randomly generated IV
    fun encryptPassword(password: String): String {
        val key = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
        val iv = generateIv()
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val ivParamSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParamSpec)
        val encrypted = cipher.doFinal(password.toByteArray())
        // Prepend IV to the encrypted data before encoding to Base64
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    // Decrypt password that includes an IV at the beginning
    fun decryptPassword(encryptedPassword: String): String {
        val decodedBytes = Base64.decode(encryptedPassword, Base64.DEFAULT)
        val iv = decodedBytes.copyOfRange(0, 16) // Extract IV from the start
        val encrypted = decodedBytes.copyOfRange(16, decodedBytes.size)
        val key = SecretKeySpec(SECRET_KEY.toByteArray(), ALGORITHM)
        val ivParamSpec = IvParameterSpec(iv)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key, ivParamSpec)
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }
}

