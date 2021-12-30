package br.com.gmfonseca.datastoresample.data.datasource.remote

import br.com.gmfonseca.datastoresample.data.model.LoggedInUser
import java.util.UUID.randomUUID

interface LoginApi {
    suspend fun login(username: String, password: String): LoggedInUser
}

class LoginApiStub : LoginApi {
    override suspend fun login(username: String, password: String): LoggedInUser {
        return requireNotNull(
            userStore[UserLoginDto(username, password)]
        ) { "Invalid user/password." }
    }
}

private data class UserLoginDto(
    val username: String,
    val password: String,
)

private val userStore: Map<UserLoginDto, LoggedInUser> = mapOf(
    UserLoginDto("gabriel", "123") to LoggedInUser(
        randomUUID().toString(), "gabriel", "Gabriel Fonseca"
    )
)