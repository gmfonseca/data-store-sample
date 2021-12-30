package br.com.gmfonseca.datastoresample.infra.security

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.BLOCK_MODE_GCM
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT

import androidx.annotation.RequiresApi

import java.nio.charset.Charset
import java.security.KeyStore

import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

// Inspired by: https://proandroiddev.com/securing-androids-datastore-ad56958ca6ee

@RequiresApi(Build.VERSION_CODES.M)
class EncryptionService(private val tools: SecurityTools) {

    fun encryptData(keyAlias: String, text: String): ByteArray =
        with(tools.cipher) {
            synchronized(this) {
                init(ENCRYPT_MODE, generateSecretKey(keyAlias))
                doFinal(text.toByteArray(charset))
            }
        }

    fun decryptData(keyAlias: String, encryptedData: ByteArray): String =
        with(tools.cipher) {
            synchronized(this) {
                init(DECRYPT_MODE, getSecretKey(keyAlias), GCMParameterSpec(128, iv))
                doFinal(encryptedData).toString(charset)
            }
        }

    private fun generateSecretKey(keyAlias: String): SecretKey = tools.keyGenerator.apply {
        init(
            KeyGenParameterSpec
                .Builder(keyAlias, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
                .setBlockModes(BLOCK_MODE_GCM)
                .setEncryptionPaddings(ENCRYPTION_PADDING_NONE)
                .build()
        )
    }.generateKey()

    private fun getSecretKey(keyAlias: String) =
        (tools.keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey

    private companion object {
        val charset: Charset get() = Charsets.UTF_8
    }
}
