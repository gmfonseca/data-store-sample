package br.com.gmfonseca.datastoresample.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gmfonseca.datastoresample.data.model.Resource
import br.com.gmfonseca.datastoresample.data.repositories.LoginRepository
import br.com.gmfonseca.datastoresample.domain.entities.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Loading)
    val user: StateFlow<Resource<User>> = _user

    fun loadUser() {
        _user.value = Resource.Loading

        viewModelScope.launch {
            delay(1_000L)
            when (val result = loginRepository.loggedUser()) {
                is Resource.Success -> result.data.collectLatest { user ->
                    user?.let { _user.emit(Resource.Success(it)) }
                        ?: _user.emit(Resource.Error(IOException("No user")))
                }
                is Resource.Error -> _user.emit(result)
                is Resource.Loading -> _user.emit(result)
            }
        }
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            _user.value = Resource.Loading
            delay(1_000L)

            val result = loginRepository.login(username, password)

            if (result is Resource.Success) {
                _user.value = Resource.Success(User(result.data.username, result.data.displayName))
            } else {
                _user.value = Resource.Error(IOException())
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            loginRepository.logout()
            _user.value = Resource.Error(IOException())
        }
    }
}