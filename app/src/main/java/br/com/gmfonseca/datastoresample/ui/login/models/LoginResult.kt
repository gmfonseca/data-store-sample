package br.com.gmfonseca.datastoresample.ui.login.models

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null,
    val loading: Boolean = false
)