package com.example.aquaminder.feature_weather.presentation.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentWeatherBinding
import com.example.aquaminder.feature_weather.domain.model.HourlyWeather
import com.example.aquaminder.feature_weather.domain.model.IconWeather
import com.example.aquaminder.feature_weather.presentation.adapter.HourlyWeatherAdapter
import com.example.aquaminder.feature_weather.presentation.view_models.WeatherViewModel
import com.example.aquaminder.feature_weather.utils.WeatherState
import com.example.aquaminder.feature_weather.utils.WeatherUtils.setWeatherIcon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var binding: FragmentWeatherBinding

    private lateinit var adapter: HourlyWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLocation.text = "Av Alberdi 1045, Ciudad de Buenos Aires"
        binding.wbIcon.setWeatherIcon(IconWeather.SUNNY)
        binding.tvTemperature.text = "22°"
        binding.tvDescription.text = "Soleado"

        adapter = HourlyWeatherAdapter(emptyList())
        binding.rvHourly.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.adapter = adapter

        val hourlyWeatherList =
            listOf(
                HourlyWeather("Ahora", IconWeather.SUNNY, "22°C", "60%"),
                HourlyWeather("12hs", IconWeather.CLOUDY, "6°C", "62%"),
                HourlyWeather("13hs", IconWeather.STORMY, "4°C", "65%"),
                HourlyWeather("14hs", IconWeather.RAINY, "9°C", "67%"),
                HourlyWeather("15hs", IconWeather.RAINY, "9°C", "60%"),
                HourlyWeather("16hs", IconWeather.CLOUDY, "6°C", "62%"),
                HourlyWeather("17hs", IconWeather.STORMY, "4°C", "65%"),
                HourlyWeather("18hs", IconWeather.SUNNY, "18°C", "67%"),
            )

        adapter.updateData(hourlyWeatherList)

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.includeProgressBar.clProgressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.weatherState.collect { weatherState ->
                        when (weatherState) {
                            is WeatherState.Success -> {
                            }

                            is WeatherState.Error -> {
                                showErrorMessage(weatherState.errorMsg)
                            }

                            is WeatherState.Idle -> {}
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