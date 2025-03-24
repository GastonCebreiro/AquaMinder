package com.example.aquaminder.feature_configuration.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentConfigurationBinding
import com.example.aquaminder.feature_configuration.presentation.view_models.ConfigurationViewModel
import com.example.aquaminder.feature_configuration.utils.ConfigurationState
import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConfigurationFragment : Fragment() {

    private val viewModel: ConfigurationViewModel by viewModels()

    private lateinit var binding: FragmentConfigurationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSwitch()

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.clProgressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.configurationState.collect { configurationState ->
                        when (configurationState) {
                            is ConfigurationState.Success -> {
                            }

                            is ConfigurationState.Error -> {
                                showErrorMessage(configurationState.errorMsg)
                            }

                            is ConfigurationState.Idle -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setSwitch() {
        binding.switchSensor.setOnCheckedChangeListener { _, isChecked ->
            binding.tvSensor.text =
                if (isChecked)
                    getString(R.string.fragment_configuration_switch_sensor_on)
                else
                    getString(R.string.fragment_configuration_switch_sensor_off)
        }
    }


    private fun showErrorMessage(message: String, logoId: Int? = null) {
        DialogUtils.showErrorDialog(
            context = requireContext(),
            imageId = logoId,
            titleText = message,
            onAcceptAction = {
                goBack()
            }
        )
    }

    private fun goBack() {
        requireActivity().finish()
    }

}