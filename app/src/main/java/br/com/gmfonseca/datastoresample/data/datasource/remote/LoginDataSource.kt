package br.com.gmfonseca.datastoresample.data.datasource.remote

import br.com.gmfonseca.datastoresample.data.model.LoggedInUser
import br.com.gmfonseca.datastoresample.data.model.Resource
import br.com.gmfonseca.datastoresample.infra.storage.datastore.PreferencesDataStore
import br.com.gmfonseca.datastoresample.infra.utils.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(
    private val loginApi: LoginApi,
    private val credentialsDataStore: PreferencesDataStore
) {

    suspend fun loggedUser(): Flow<LoggedInUser?> {
        return credentialsDataStore.readData().map {
            try {
                json.decodeFromString<LoggedInUser>(it)
            } catch (t: NoSuchElementException) {
                null
            } catch (t: SerializationException) {
                credentialsDataStore.clearDataStore()
                null
            }
        }
    }

    suspend fun login(username: String, password: String): Resource<LoggedInUser> {
        return try {
            val user = loginApi.login(username, password)
                .also { LoggedInUser(it.userId, username, it.displayName) }

            credentialsDataStore.setData(json.encodeToString(user))

            Resource.Success(user)
        } catch (t: Throwable) {
            Resource.Error(IOException("Error logging in", t))
        }
    }

    suspend fun invalidateUser() {
        credentialsDataStore.clearDataStore()
    }
}
