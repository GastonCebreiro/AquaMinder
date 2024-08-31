package com.example.aquaminder.feature_login.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aquaminder.R
import com.example.aquaminder.databinding.FragmentLoginBinding
import com.example.aquaminder.feature_login.presentation.view_model.LoginViewModel
import com.example.aquaminder.feature_login.utils.LoginState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        clearEntries()
        cleanErrors()

        viewModel.getKeepValues()

        binding.btnLogin.setOnClickListener {
            loginButtonAction()
        }

        binding.btnRegister.setOnClickListener {
            registerButtonAction()
        }

        binding.btnForgotPassword.setOnClickListener {
            forgotPasswordButtonAction()
        }

        binding.cbKeepValues.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setKeepValues(isChecked)
        }


        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { loginState ->
                when (loginState) {
                    is LoginState.Success -> {
                        cleanErrors()
                        showMessage(loginState.msg)
                        navToMainActivity()
                    }
                    is LoginState.Error -> {
                        cleanErrors()
                        showMessage(loginState.errorMsg)
                    }
                    is LoginState.WrongName -> {
                        setUserNameError(loginState.errorMsg)
                        showMessage(loginState.errorMsg)
                    }
                    is LoginState.WrongPassword -> {
                        setUserPasswordError(loginState.errorMsg)
                        showMessage(loginState.errorMsg)
                    }
                    is LoginState.UsernameNotFound -> {
                        setUserNameError(loginState.errorMsg)
                        showMessage(loginState.errorMsg)
                    }
                    is LoginState.UserSavedValues -> {
                        setUserSavedName(loginState.name)
                        setUserSavedPassword(loginState.password)
                    }
                    is LoginState.KeepValues -> {
                        setCheckBoxKeepValues(loginState.isChecked)
                    }
                    is LoginState.NewPasswordSent -> {
                        showMessage(loginState.msg)
                    }
                    is LoginState.Loading -> {
                        binding.progressBar.isVisible = loginState.isLoading
                    }
                }
            }
        }
    }


    private fun clearEntries() {
        binding.etInputName.setText("")
        binding.etInputName.clearFocus()
        binding.etInputPassword.setText("")
        binding.etInputPassword.clearFocus()
    }


    private fun loginButtonAction() {
        viewModel.checkUserSelected(
            binding.etInputName.text.toString().trim(),
            binding.etInputPassword.text.toString().trim()
        )
    }


    private fun registerButtonAction() {
        navToNewUserFragment()
    }

    private fun forgotPasswordButtonAction() {
        viewModel.getNewPassword(
            binding.etInputName.text.toString().trim(),
        )
    }

    private fun setUserNameError(errorMsg: String) {
        cleanErrors()
        binding.lInputName.error = errorMsg
    }

    private fun setUserPasswordError(errorMsg: String) {
        cleanErrors()
        binding.lInputPassword.error = errorMsg
    }

    private fun setUserSavedPassword(password: String) {
        binding.etInputPassword.setText(password)
        binding.lInputPassword.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.autofill_input
        )
    }

    private fun setUserSavedName(name: String) {
        binding.etInputName.setText(name)
        binding.lInputName.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.autofill_input
        )
    }

    private fun setCheckBoxKeepValues(isChecked: Boolean) {
        binding.cbKeepValues.isChecked = isChecked
    }

    private fun cleanErrors() {
        binding.lInputName.error = null
        binding.lInputPassword.error = null
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun navToMainActivity() {
        val action = LoginFragmentDirections.actionLoginFragmentToMainActivity()
        findNavController().navigate(action)
    }

    private fun navToNewUserFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToNewUserFragment()
        findNavController().navigate(action)
    }


}