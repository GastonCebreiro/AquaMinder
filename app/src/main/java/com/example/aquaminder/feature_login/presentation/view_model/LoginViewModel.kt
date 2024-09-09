package com.example.aquaminder.feature_login.presentation.view_model

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.model.request.NewPasswordRequestDomainModel
import com.example.aquaminder.feature_login.domain.use_case.AskNewPasswordUseCase
import com.example.aquaminder.feature_login.domain.use_case.GetKeepValuesUseCase
import com.example.aquaminder.feature_login.domain.use_case.GetUserLoggedUseCase
import com.example.aquaminder.feature_login.domain.use_case.GetUserUseCase
import com.example.aquaminder.feature_login.domain.use_case.SaveKeepValuesUseCase
import com.example.aquaminder.feature_login.domain.use_case.SaveUserLoggedUseCase
import com.example.aquaminder.feature_login.utils.LoginState
import com.example.aquaminder.feature_login.utils.NewUserState
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

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun checkUserSelected(nameEntry: String, passwordEntry: String) {
        _loginState.value = LoginState.Idle
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

            passwordEntry.length < 4 -> {
                _loginState.value =
                    LoginState.WrongPassword(resources.getString(R.string.error_msg_short_password))
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
        _loginState.value = LoginState.Idle
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
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val request = NewPasswordRequestDomainModel(name)
            askNewPasswordUseCase.invoke(request)
                .collect { res ->
                    when (res) {
                        is ResultEvent.Success -> {
                            _loginState.value = LoginState.NewPasswordSent(res.data)
                        }

                        is ResultEvent.Error -> {
                            when (res.error) {
                                is AppError.UsernameNotFound -> {
                                    _loginState.value =
                                        LoginState.Error(resources.getString(R.string.error_msg_new_user_not_found))
                                }

                                is AppError.GenericError -> {
                                    _loginState.value = LoginState.Error(res.error.errorMsg)
                                }

                                else -> {
                                    _loginState.value = LoginState.Error("")
                                }
                            }

                        }
                    }
                    _isLoading.value = false
                }
        }
    }

    private fun getUserSelected(
        name: String,
        password: String
    ) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            getUserUseCase.invoke(name, password)
                .collect { res ->
                    when (res) {
                        is ResultEvent.Success -> {

                            saveUserLoggedUseCase.invoke(res.data)

                            _loginState.value =
                                LoginState.Success(resources.getString(R.string.success_msg_login_successful))
                        }

                        is ResultEvent.Error -> {
                            when (res.error) {
                                is AppError.WrongPassword -> {
                                    _loginState.value =
                                        LoginState.WrongPassword(resources.getString(R.string.error_msg_invalid_password))
                                }

                                is AppError.UsernameNotFound -> {
                                    _loginState.value =
                                        LoginState.UsernameNotFound(resources.getString(R.string.error_msg_new_user_not_found))
                                }

                                is AppError.NetworkError -> {
                                    _loginState.value =
                                        LoginState.Error(
                                            resources.getString(R.string.error_msg_network),
                                            R.drawable.ic_error_network
                                        )
                                }

                                else -> {
                                    _loginState.value = LoginState.Error("")
                                }
                            }

                        }
                    }
                    _isLoading.value = false
                }
        }
    }

}