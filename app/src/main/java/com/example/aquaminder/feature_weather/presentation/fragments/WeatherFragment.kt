package com.example.aquaminder.feature_weather.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentWeatherBinding
import com.example.aquaminder.feature_weather.domain.model.HourlyWeatherDomainModel
import com.example.aquaminder.feature_weather.domain.model.WeatherDomainModel
import com.example.aquaminder.feature_weather.domain.model.getAddressFormatted
import com.example.aquaminder.feature_weather.domain.model.getColorByIcon
import com.example.aquaminder.feature_weather.domain.model.getHumidityFormatted
import com.example.aquaminder.feature_weather.domain.model.getTemperatureFormatted
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

        viewModel.getWeather()

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        setLayoutVisibility(!isLoading)
                        binding.includeProgressBar.clProgressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.weatherState.collect { weatherState ->
                        when (weatherState) {
                            is WeatherState.Success -> {
                                setWeatherData(weatherState.weather)
                            }

                            is WeatherState.Error -> {
                                showErrorMessage(weatherState.errorMsg, weatherState.logoId)
                            }

                            is WeatherState.Idle -> {}
                        }
                    }
                }
            }
        }
    }

    private fun setWeatherData(weather: WeatherDomainModel) {
        binding.tvLocation.text = weather.getAddressFormatted()
        binding.wbIcon.setWeatherIcon(weather.icon)
        binding.tvTemperature.text = weather.getTemperatureFormatted()
        binding.tvDescription.text = weather.description
        binding.tvDescription.setTextColor(ContextCompat.getColor(requireContext(), weather.getColorByIcon()))
        binding.tvHumidity.text = weather.getHumidityFormatted()

        setAdapter(weather.hourlyWeather)
    }

    private fun setAdapter(hourlyWeather: List<HourlyWeatherDomainModel>) {
        adapter = HourlyWeatherAdapter(hourlyWeather)
        binding.rvHourly.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.adapter = adapter
    }


    private fun showErrorMessage(message: String, logoId: Int? = null) {
        setLayoutVisibility(false)
        DialogUtils.showErrorDialog(
            context = requireContext(),
            imageId = logoId,
            titleText = message,
            onAcceptAction = {
                goBack()
            }
        )
    }

    private fun setLayoutVisibility(isVisible: Boolean) {
        binding.cvWeather.isVisible = isVisible
        binding.tvNextHours.isVisible = isVisible
        binding.rvHourly.isVisible = isVisible
    }

    private fun goBack() {
        findNavController().navigateUp()
    }

}