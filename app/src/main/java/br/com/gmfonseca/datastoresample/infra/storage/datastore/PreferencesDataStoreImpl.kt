package br.com.gmfonseca.datastoresample.infra.storage.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class PreferencesDataStoreImpl(
    private val dataStore: DataStore<Preferences>
) : PreferencesDataStore {
    override fun readData() = dataStore.data.map { preferences ->
        preferences[DATA].orEmpty()
    }

    override suspend fun setData(value: String) {
        dataStore.edit {
            it[DATA] = value
        }
    }

    override suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }

    private companion object {
        val DATA = stringPreferencesKey("data")
    }
}