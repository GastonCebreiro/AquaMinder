package com.example.aquaminder.feature_login.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.AppError
import com.example.aquaminder.core.utils.ResultEvent
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.use_case.InsertUserUseCase
import com.example.aquaminder.feature_login.utils.NewUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewUserViewModel @Inject constructor(
    private val resources: Resources,
    private val insertUserUseCase: InsertUserUseCase
) : ViewModel() {

    private val _newUserState = MutableStateFlow<NewUserState>(NewUserState.Idle)
    val newUserState: StateFlow<NewUserState> = _newUserState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun checkUserToRegister(nameEntry: String, mailEntry: String, passwordEntry: String) {
        _newUserState.value = NewUserState.Idle
        when {
            mailEntry.isBlank() -> {
                _newUserState.value =
                    NewUserState.WrongMail(resources.getString(R.string.error_msg_enter_mail))
                return
            }

            !isValidEmail(mailEntry) -> {
                _newUserState.value =
                    NewUserState.WrongMail(resources.getString(R.string.error_msg_format_mail))
                return
            }

            nameEntry.isBlank() -> {
                _newUserState.value =
                    NewUserState.WrongName(resources.getString(R.string.error_msg_enter_name))
                return
            }

            passwordEntry.isBlank() -> {
                _newUserState.value =
                    NewUserState.WrongPassword(resources.getString(R.string.error_msg_enter_password))
                return
            }

            passwordEntry.length < 4 -> {
                _newUserState.value =
                    NewUserState.WrongPassword(resources.getString(R.string.error_msg_short_password))
                return
            }

            else -> {
                registerUser(
                    name = nameEntry,
                    mail = mailEntry,
                    password = passwordEntry
                )
            }
        }
    }

    private fun registerUser(
        name: String,
        mail: String,
        password: String
    ) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val userToInsert = UserDomainModel(
                name = name,
                mail = mail,
                password = password
            )
            insertUserUseCase.invoke(userToInsert)
                .collect { res ->
                    when (res) {
                        is ResultEvent.Success -> {
                            _newUserState.value =
                                NewUserState.Success(resources.getString(R.string.success_msg_user_registered))
                        }

                        is ResultEvent.Error -> {
                            when (res.error) {
                                is AppError.GenericError -> {
                                    _newUserState.value = NewUserState.Error(
                                        resources.getString(
                                            R.string.error_msg_catch, res.error.errorMsg
                                        )
                                    )

                                }

                                is AppError.MailUnavailable -> {
                                    _newUserState.value = NewUserState.Error(
                                        resources.getString(
                                            R.string.error_msg_new_user_mail_unavailable
                                        )
                                    )
                                }

                                is AppError.UsernameUnavailable -> {
                                    _newUserState.value = NewUserState.Error(
                                        resources.getString(
                                            R.string.error_msg_new_user_username_unavailable
                                        )
                                    )
                                }

                                is AppError.NetworkError -> {
                                    _newUserState.value =
                                        NewUserState.Error(
                                            resources.getString(R.string.error_msg_network),
                                            R.drawable.ic_error_network
                                        )
                                }

                                else -> {
                                    _newUserState.value = NewUserState.Error("")
                                }
                            }
                        }
                    }
                    _isLoading.value = false
                }
        }
    }

    private fun isValidEmail(mail: String): Boolean = mail.matches(EMAIL_REGEX.toRegex())

    companion object {
        private const val EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    }
}

