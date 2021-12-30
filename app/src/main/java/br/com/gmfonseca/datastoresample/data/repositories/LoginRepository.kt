package br.com.gmfonseca.datastoresample.data.repositories

import br.com.gmfonseca.datastoresample.data.datasource.remote.LoginDataSource
import br.com.gmfonseca.datastoresample.data.model.LoggedInUser
import br.com.gmfonseca.datastoresample.data.model.Resource
import br.com.gmfonseca.datastoresample.domain.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class LoginRepository(val dataSource: LoginDataSource) {

    private var user: User? = null

    suspend fun logout() {
        user = null
        dataSource.invalidateUser()
    }

    suspend fun login(username: String, password: String): Resource<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Resource.Success) {
            setLoggedInUser(result.data.asUser)
        }

        return result
    }

    suspend fun loggedUser(): Resource<Flow<User?>> {
        val user = user

        return if (user == null) {
            Resource.Success(
                dataSource.loggedUser().map { result ->
                    result?.asUser.also(::setLoggedInUser)
                }
            )
        } else {
            Resource.Success(flow { emit(user) })
        }
    }

    private fun setLoggedInUser(user: User?) {
        this.user = user
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}

private val LoggedInUser.asUser get() = User(username, displayName)
