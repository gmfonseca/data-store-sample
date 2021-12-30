package br.com.gmfonseca.datastoresample.data.model

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Resource<out T : Any> {

    object Loading : Resource<Nothing>()
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success(data=$data)"
            is Loading -> "Loading"
            is Error -> "Error(exception=$exception)"
        }
    }
}

inline fun <T : Any, R : Any> Resource<T>.map(transform: (T) -> R): Resource<R> = when (this) {
    is Resource.Success -> Resource.Success(transform(data))
    is Resource.Error -> Resource.Error(exception)
    is Resource.Loading -> Resource.Loading
}
