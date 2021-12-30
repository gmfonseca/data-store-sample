package br.com.gmfonseca.datastoresample.infra.security

import android.os.Build
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

@RequiresApi(Build.VERSION_CODES.M)
class SecurityTools {
    val keyStore: KeyStore by lazy {
        KeyStore.getInstance(PROVIDER).apply {
            load(null)
        }
    }

    val keyGenerator: KeyGenerator by lazy {
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER)
    }

    val cipher: Cipher by lazy {
        Cipher.getInstance(ALGORITHM)
    }

    private companion object {
        const val PROVIDER = "AndroidKeyStore"
        const val ALGORITHM = "AES/GCM/NoPadding"
    }
}