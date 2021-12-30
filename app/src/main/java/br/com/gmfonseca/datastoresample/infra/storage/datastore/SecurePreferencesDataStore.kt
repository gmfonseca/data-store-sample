package br.com.gmfonseca.datastoresample.infra.storage.datastore

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import br.com.gmfonseca.datastoresample.infra.security.EncryptionService
import br.com.gmfonseca.datastoresample.infra.utils.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@RequiresApi(Build.VERSION_CODES.M)
class SecurePreferencesDataStore(
    private val dataStore: DataStore<Preferences>,
    private val encryption: EncryptionService
) : PreferencesDataStore {

    override suspend fun setData(value: String) {
        dataStore.secureEdit(value) { prefs, encryptedValue ->
            prefs[SECURED_DATA] = encryptedValue
        }
    }

    override suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }

    override fun readData(): Flow<String> = dataStore.data.secureMap(
        { preferences -> preferences[SECURED_DATA].orEmpty() },
        defaultValue = { "" }
    )

    private suspend inline fun <reified T> DataStore<Preferences>.secureEdit(
        value: T,
        crossinline editStore: (MutablePreferences, String) -> Unit
    ) {
        edit {
            val encryptedValue = encryption.encryptData(
                SECURITY_KEY_ALIAS, Json.encodeToString(value)
            )

            editStore(it, encryptedValue.joinToString(BYTES_SEPARATOR))
        }
    }

    private inline fun <reified T> Flow<Preferences>.secureMap(
        crossinline fetchValue: (value: Preferences) -> String,
        crossinline defaultValue: () -> T
    ): Flow<T> = map {
        try {
            val decryptedValue = encryption.decryptData(
                SECURITY_KEY_ALIAS,
                fetchValue(it).split(BYTES_SEPARATOR).map(String::toByte).toByteArray()
            )

            json.decodeFromString(decryptedValue)
        } catch (t: Throwable) {
            defaultValue()
        }

    }

    private companion object {
        private const val SECURITY_KEY_ALIAS = "data-store"
        private const val BYTES_SEPARATOR = "|"

        val SECURED_DATA = stringPreferencesKey("secured_data")
    }
}
