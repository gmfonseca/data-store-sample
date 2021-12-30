package br.com.gmfonseca.datastoresample.ui.login.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.gmfonseca.datastoresample.R
import br.com.gmfonseca.datastoresample.data.datasource.remote.LoginApiStub
import br.com.gmfonseca.datastoresample.data.datasource.remote.LoginDataSource
import br.com.gmfonseca.datastoresample.data.repositories.LoginRepository
import br.com.gmfonseca.datastoresample.infra.storage.datastore.PreferencesDataStoreImpl
import br.com.gmfonseca.datastoresample.ui.login.viewmodel.LoginViewModel
import br.com.gmfonseca.datastoresample.ui.login.viewmodel.dataStore

@Composable
fun LoginFormScreen(loginViewModel: LoginViewModel) {
    var name by remember { mutableStateOf("gabriel") }
    var password by remember { mutableStateOf("123") }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text(stringResource(id = R.string.prompt_email), fontWeight = FontWeight.Bold)
        OutlinedTextField(value = name, onValueChange = { name = it })

        Text(
            stringResource(id = R.string.prompt_password),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { loginViewModel.login(name, password) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.action_sign_in_short))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormScreenPreview() {
    LoginFormScreen(
        LoginViewModel(
            LoginRepository(
                LoginDataSource(
                    LoginApiStub(),
                    PreferencesDataStoreImpl(LocalContext.current.dataStore)
                )
            )
        )
    )
}