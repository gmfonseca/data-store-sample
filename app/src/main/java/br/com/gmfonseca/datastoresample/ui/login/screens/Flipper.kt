package br.com.gmfonseca.datastoresample.ui.login.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import br.com.gmfonseca.datastoresample.data.model.Resource
import br.com.gmfonseca.datastoresample.ui.login.viewmodel.LoginViewModel

@Composable
fun Flipper(viewModel: LoginViewModel) {
    val state = viewModel.user.collectAsState()

    when (val result = state.value) {
        is Resource.Success -> HomeScreen(user = result.data) { viewModel.logout() }
        is Resource.Loading -> LoadingScreen()
        is Resource.Error -> LoginFormScreen(viewModel)
    }
}
