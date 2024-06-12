package com.jay.passwordmanager.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object AesEncryption {
    private const val KEY_ALIAS = "my_key_alias"
    private const val KEY_STORE_PROVIDER = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val KEY_SIZE = 256
    private const val IV_SIZE = 12 // 96-bit IV for GCM mode

    fun generateKey(): SecretKey {
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .build()

        val keyGenerator = KeyGenerator.getInstance("AES", KEY_STORE_PROVIDER)
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    fun encrypt(password: String, key: SecretKey): Pair<String, String> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(password.toByteArray())
        return Pair(Base64.encodeToString(iv, Base64.DEFAULT), Base64.encodeToString(encryptedBytes, Base64.DEFAULT))
    }

    fun decrypt(encryptedPassword: String, key: SecretKey): Pair<String, String> {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val encryptedBytes = Base64.decode(encryptedPassword, Base64.DEFAULT)
        val ivBytes = encryptedBytes.copyOfRange(0, IV_SIZE)
        val encryptedData = encryptedBytes.copyOfRange(IV_SIZE, encryptedBytes.size)
        val gcmParameterSpec = GCMParameterSpec(128, ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec)
        val decryptedBytes = cipher.doFinal(encryptedData)
        val decryptedPassword = String(decryptedBytes)
        val iv = Base64.encodeToString(ivBytes, Base64.DEFAULT)
        return Pair(decryptedPassword, iv)
    }
}