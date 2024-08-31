package com.example.aquaminder.feature_login.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.feature_login.domain.use_case.AskNewPasswordUseCase
import com.example.aquaminder.feature_login.domain.use_case.GetKeepValuesUseCase
import com.example.aquaminder.feature_login.domain.use_case.GetUserLoggedUseCase
import com.example.aquaminder.feature_login.domain.use_case.GetUserUseCase
import com.example.aquaminder.feature_login.domain.use_case.SaveKeepValuesUseCase
import com.example.aquaminder.feature_login.domain.use_case.SaveUserLoggedUseCase
import com.example.aquaminder.feature_login.utils.LoginEvent
import com.example.aquaminder.feature_login.utils.LoginState
import com.example.aquaminder.feature_login.utils.NewPasswordEvent
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val resources: Resources,
    private val getUserUseCase: GetUserUseCase,
    private val askNewPasswordUseCase: AskNewPasswordUseCase,
    private val saveUserLoggedUseCase: SaveUserLoggedUseCase,
    private val saveKeepValuesUseCase: SaveKeepValuesUseCase,
    private val getUserLoggedUseCase: GetUserLoggedUseCase,
    private val getKeepValuesUseCase: GetKeepValuesUseCase,
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading(false))
    val loginState: StateFlow<LoginState> = _loginState


    fun checkUserSelected(nameEntry: String, passwordEntry: String) {
        when {
            nameEntry.isBlank() -> {
                _loginState.value =
                    LoginState.WrongName(resources.getString(R.string.error_msg_enter_name))
                return
            }

            passwordEntry.isBlank() -> {
                _loginState.value =
                    LoginState.WrongPassword(resources.getString(R.string.error_msg_enter_password))
                return
            }

            else -> {
                getUserSelected(
                    name = nameEntry,
                    password = passwordEntry
                )
            }
        }
    }

    fun getNewPassword(nameEntry: String) {
        when {
            nameEntry.isBlank() -> {
                _loginState.value =
                    LoginState.WrongName(resources.getString(R.string.error_msg_enter_name))
                return
            }

            else -> {
                askNewPassword(nameEntry)
            }
        }
    }

    fun setKeepValues(isChecked: Boolean) {
        saveKeepValuesUseCase.invoke(isChecked)
    }

    fun getKeepValues() {
        val isChecked = getKeepValuesUseCase.invoke()

        _loginState.value = LoginState.KeepValues(isChecked)

        if (!isChecked) return

        when (val res = getUserLoggedUseCase.invoke()) {
            is ResultEvent.Success -> {
                val userLogged = res.data
                _loginState.value = LoginState.UserSavedValues(userLogged.name, userLogged.password)
            }
            is ResultEvent.Error -> {
                return
            }
        }
    }


    private fun askNewPassword(name: String) {
        _loginState.value = LoginState.Loading(true)

        viewModelScope.launch(Dispatchers.IO) {
            val request = NewPasswordRequestDomainModel(name)
            askNewPasswordUseCase.invoke(request)
                .collect { res ->
                    when (res) {
                        is NewPasswordEvent.Success -> {
                            _loginState.value = LoginState.NewPasswordSent(res.msg)
                        }

                        is NewPasswordEvent.Error -> {
                            _loginState.value = LoginState.Error(res.errorMsg)
                        }

                        is NewPasswordEvent.UsernameNotFound -> {
                            _loginState.value = LoginState.UsernameNotFound(res.errorMsg)
                        }
                    }
                    _loginState.value = LoginState.Loading(false)
                }
        }
    }

    private fun getUserSelected(
        name: String,
        password: String
    ) {
        _loginState.value = LoginState.Loading(true)

        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase.invoke(name, password)
                .collect { res ->
                    when (res) {
                        is LoginEvent.Success -> {

                            saveUserLoggedUseCase.invoke(res.user)

                            _loginState.value = LoginState.Success(res.msg)
                        }

                        is LoginEvent.Error -> {
                            _loginState.value = LoginState.Error(res.errorMsg)
                        }

                        is LoginEvent.WrongPassword -> {
                            _loginState.value = LoginState.WrongPassword(res.errorMsg)
                        }

                        is LoginEvent.UsernameNotFound -> {
                            _loginState.value = LoginState.UsernameNotFound(res.errorMsg)
                        }
                    }
                    _loginState.value = LoginState.Loading(false)
                }
        }
    }

}