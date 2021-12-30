package br.com.gmfonseca.datastoresample.data.model

import br.com.gmfonseca.datastoresample.infra.utils.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
data class LoggedInUser(
    val userId: String,
    val username: String,
    val displayName: String,
)

fun main() {
    val user = LoggedInUser(
        userId = "ce450399-f50c-4403-a67c-9da420b23b84",
        username = "gabriel",
        displayName = "Gabriel Fonseca"
    )

    print(
        json.encodeToString(user)
    )
}
