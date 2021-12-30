package br.com.gmfonseca.datastoresample.ui.login.viewmodel

import android.content.Context
import android.os.Build
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.gmfonseca.datastoresample.data.datasource.remote.LoginApiStub
import br.com.gmfonseca.datastoresample.data.datasource.remote.LoginDataSource
import br.com.gmfonseca.datastoresample.data.repositories.LoginRepository
import br.com.gmfonseca.datastoresample.infra.security.EncryptionService
import br.com.gmfonseca.datastoresample.infra.security.SecurityTools
import br.com.gmfonseca.datastoresample.infra.storage.datastore.PreferencesDataStoreImpl
import br.com.gmfonseca.datastoresample.infra.storage.datastore.SecurePreferencesDataStore

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val preferencesDataStore by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SecurePreferencesDataStore(context.dataStore, EncryptionService(SecurityTools()))
            PreferencesDataStoreImpl(context.dataStore)
        } else {
            PreferencesDataStoreImpl(context.dataStore)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(
                        LoginApiStub(),
                        preferencesDataStore
                    )
                )
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

val Context.dataStore by preferencesDataStore(name = "data-store")
