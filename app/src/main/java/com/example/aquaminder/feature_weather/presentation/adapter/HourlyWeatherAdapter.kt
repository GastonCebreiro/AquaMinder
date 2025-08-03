package com.example.aquaminder.feature_weather.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aquaminder.databinding.ItemHourlyWeatherBinding
import com.example.aquaminder.feature_weather.domain.model.HourlyWeather
import com.example.aquaminder.feature_weather.utils.WeatherUtils.setWeatherIcon

class HourlyWeatherAdapter(
    private var items: List<HourlyWeather>
) : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val binding = ItemHourlyWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HourlyWeather>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class HourlyWeatherViewHolder(private val binding: ItemHourlyWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HourlyWeather) {
            binding.tvHour.text = item.hour
            binding.tvTemperature.text = item.temperature
            binding.tvHumidity.text = item.humidity
            binding.wbIcon.setWeatherIcon(item.icon, isSmall = true)
        }
    }
}
