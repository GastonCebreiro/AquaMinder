package com.example.aquaminder.feature_login.presentation.view_model

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aquaminder.R
import com.example.aquaminder.feature_login.domain.model.UserDomainModel
import com.example.aquaminder.feature_login.domain.use_case.InsertUserUseCase
import com.example.aquaminder.feature_login.utils.InsertUserEvent
import com.example.aquaminder.feature_login.utils.NewUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewUserViewModel @Inject constructor(
    private val resources: Resources,
    private val insertUserUseCase: InsertUserUseCase
) : ViewModel() {

    private val _newUserState = MutableStateFlow<NewUserState>(NewUserState.Loading(false))
    val newUserState: StateFlow<NewUserState> = _newUserState


    fun checkUserToRegister(nameEntry: String, mailEntry: String, passwordEntry: String) {
        when {
            mailEntry.isBlank() -> {
                _newUserState.value =
                    NewUserState.WrongMail(resources.getString(R.string.error_msg_enter_mail))
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
        _newUserState.value = NewUserState.Loading(true)

        viewModelScope.launch(Dispatchers.IO) {
            val userToInsert = UserDomainModel(
                name = name,
                mail = mail,
                password = password
            )
            insertUserUseCase.invoke(userToInsert)
                .collect { res ->
                    when (res) {
                        is InsertUserEvent.Success -> {
                            _newUserState.value = NewUserState.Success(resources.getString(R.string.success_msg_user_registered))
                        }
                        is InsertUserEvent.Error -> {
                            _newUserState.value = NewUserState.Error(res.errorMsg)
                        }
                    }
                    //_newUserState.value = NewUserState.Loading(false)
                }
        }
    }
}

