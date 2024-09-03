package com.example.aquaminder.feature_login.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentNewUserBinding
import com.example.aquaminder.feature_login.presentation.view_model.NewUserViewModel
import com.example.aquaminder.feature_login.utils.NewUserState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewUserFragment : Fragment() {

    private val viewModel: NewUserViewModel by viewModels()

    private lateinit var binding: FragmentNewUserBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        clearEntries()
        cleanErrors()

        binding.btnRegister.setOnClickListener {
            registerButtonAction()
        }

        binding.btnLogin.setOnClickListener {
            loginButtonAction()
        }

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.isLoading.collect { isLoading ->
                    binding.progressBar.isVisible = isLoading
                }
            }

            launch {
                viewModel.newUserState.collect { newUserState ->
                    when (newUserState) {
                        is NewUserState.Success -> {
                            cleanErrors()
                            showMessage(newUserState.msg)
                            navToLoginFragment()
                        }

                        is NewUserState.Error -> {
                            cleanErrors()
                            showErrorMessage(newUserState.errorMsg, newUserState.logoId)
                        }

                        is NewUserState.WrongName -> {
                            setUserNameError(newUserState.errorMsg)
                        }

                        is NewUserState.WrongMail -> {
                            setUserMailError(newUserState.errorMsg)
                        }

                        is NewUserState.WrongPassword -> {
                            setUserPasswordError(newUserState.errorMsg)
                        }

                        is NewUserState.Idle -> {}
                    }
                    viewModel.updateViewState(NewUserState.Idle)
                    screenEnabled(true)
                }
            }
        }

    }


    private fun clearEntries() {
        binding.etUserName.setText("")
        binding.etUserMail.setText("")
        binding.etUserMail.clearFocus()
        binding.etUserPassword.setText("")
        binding.etUserPassword.clearFocus()
    }

    private fun registerButtonAction() {
        cleanErrors()
        screenEnabled(false)
        viewModel.checkUserToRegister(
            binding.etUserName.text.toString().trim(),
            binding.etUserMail.text.toString().trim(),
            binding.etUserPassword.text.toString().trim()
        )
    }

    private fun loginButtonAction() {
        navToLoginFragment()
    }

    private fun setUserNameError(errorMsg: String) {
        cleanErrors()
        binding.lUserName.error = errorMsg
    }

    private fun setUserMailError(errorMsg: String) {
        cleanErrors()
        binding.lUserMail.error = errorMsg
    }

    private fun setUserPasswordError(errorMsg: String) {
        cleanErrors()
        binding.lUserPassword.error = errorMsg
    }

    private fun cleanErrors() {
        binding.lUserName.error = null
        binding.lUserMail.error = null
        binding.lUserPassword.error = null
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessage(message: String, logoId: Int? = null) {
        DialogUtils.showErrorDialog(
            context = requireContext(),
            imageId = logoId,
            titleText = message,
        )
    }

    private fun navToLoginFragment() {
        findNavController().navigateUp()
    }

    private fun screenEnabled(isEnabled: Boolean) {
        binding.btnRegister.isEnabled = isEnabled
        binding.btnLogin.isEnabled = isEnabled
        binding.etUserMail.isEnabled = isEnabled
        binding.etUserName.isEnabled = isEnabled
        binding.etUserPassword.isEnabled = isEnabled
    }

}