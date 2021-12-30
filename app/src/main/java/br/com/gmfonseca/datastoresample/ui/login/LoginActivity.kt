package br.com.gmfonseca.datastoresample.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.gmfonseca.datastoresample.ui.login.screens.Flipper
import br.com.gmfonseca.datastoresample.ui.login.viewmodel.LoginViewModel
import br.com.gmfonseca.datastoresample.ui.login.viewmodel.LoginViewModelFactory

class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by lazy {
        LoginViewModelFactory(applicationContext).create(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Flipper(loginViewModel)
        }

        loginViewModel.loadUser()
    }
}
