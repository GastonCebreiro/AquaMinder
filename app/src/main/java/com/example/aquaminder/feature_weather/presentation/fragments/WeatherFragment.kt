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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.FragmentWeatherBinding
import com.example.aquaminder.feature_main.domain.model.Address
import com.example.aquaminder.feature_weather.domain.model.HourlyWeather
import com.example.aquaminder.feature_weather.domain.model.IconWeather
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

        val hourlyWeatherList =
            listOf(
                HourlyWeather("19:00", IconWeather.SUNNY, 22, 60),
                HourlyWeather("20:00", IconWeather.CLOUDY, 6, 62),
                HourlyWeather("21:00", IconWeather.STORMY, 4, 65),
                HourlyWeather("22:00", IconWeather.RAINY, 9, 67),
                HourlyWeather("23:00", IconWeather.RAINY, 9, 60),
                HourlyWeather("00:00", IconWeather.CLOUDY, 6, 62),
                HourlyWeather("01:00", IconWeather.STORMY, 4, 65),
                HourlyWeather("02:00", IconWeather.SUNNY, 18, 67),
                HourlyWeather("03:00", IconWeather.SUNNY, 22, 60),
                HourlyWeather("04:00", IconWeather.CLOUDY, 6, 62),
                HourlyWeather("05:00", IconWeather.STORMY, 4, 65),
                HourlyWeather("06:00", IconWeather.RAINY, 9, 67),
                HourlyWeather("07:00", IconWeather.RAINY, 9, 60),
                HourlyWeather("08:00", IconWeather.CLOUDY, 6, 62),
                HourlyWeather("09:00", IconWeather.STORMY, 4, 65),
                HourlyWeather("10:00", IconWeather.SUNNY, 18, 67),
                HourlyWeather("11:00", IconWeather.SUNNY, 22, 60),
                HourlyWeather("12:00", IconWeather.CLOUDY, 6, 62),
                HourlyWeather("13:00", IconWeather.STORMY, 4, 65),
                HourlyWeather("14:00", IconWeather.RAINY, 9, 67),
                HourlyWeather("15:00", IconWeather.RAINY, 9, 60),
                HourlyWeather("16:00", IconWeather.CLOUDY, 6, 62),
                HourlyWeather("17:00", IconWeather.STORMY, 4, 65),
                HourlyWeather("18:00", IconWeather.SUNNY, 18, 67),
            )

        val weather = WeatherDomainModel(
            icon = IconWeather.SUNNY,
            temperature = 22,
            description = "Soleado",
            humidity = 80,
            address = Address(
                street = "Av Alberdi",
                number = "1041",
                city = "Ciudad de Buenos Aires"
            ),
            hourlyWeather = hourlyWeatherList
        )

        binding.tvLocation.text = weather.getAddressFormatted()
        binding.wbIcon.setWeatherIcon(weather.icon)
        binding.tvTemperature.text = weather.getTemperatureFormatted()
        binding.tvDescription.text = weather.description
        binding.tvDescription.setTextColor(ContextCompat.getColor(requireContext(), weather.getColorByIcon()))
        binding.tvHumidity.text = weather.getHumidityFormatted()

        adapter = HourlyWeatherAdapter(emptyList())
        binding.rvHourly.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHourly.adapter = adapter

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