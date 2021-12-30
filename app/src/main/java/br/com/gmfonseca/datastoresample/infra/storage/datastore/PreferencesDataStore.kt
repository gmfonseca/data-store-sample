package br.com.gmfonseca.datastoresample.infra.storage.datastore

import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore {
    fun readData(): Flow<String>
    suspend fun setData(value: String)
    suspend fun clearDataStore()
}
