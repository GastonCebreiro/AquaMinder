package com.example.aquaminder.feature_weather.presentation.fragments

import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentConfigurationBinding
import com.example.aquaminder.databinding.FragmentWeatherBinding
import com.example.aquaminder.feature_configuration.presentation.view_models.ConfigurationViewModel
import com.example.aquaminder.feature_configuration.utils.ConfigurationState
import com.example.aquaminder.feature_home.domain.model.IrrigationZoneDetailsDomainModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: ConfigurationViewModel by viewModels()

    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = binding.wbIcon.apply {
            setBackgroundColor(Color.TRANSPARENT)
            settings.javaScriptEnabled = true
            loadUrl("file:///android_asset/rainy.html")
        }

        binding.tvTemperature.text = "22"
        binding.tvDescription.text = "LLUVIOSO"
        binding.tvHumidity.text = "54%"
        binding.tvRainProbability.text = "17%"
        binding.tvWind.text = "16 km/h"


        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.includeProgressBar.clProgressBar.isVisible = isLoading
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