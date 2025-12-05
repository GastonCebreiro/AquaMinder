package com.example.aquaminder.feature_weather.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aquaminder.databinding.ItemHourlyWeatherBinding
import com.example.aquaminder.feature_weather.domain.model.HourlyWeatherDomainModel
import com.example.aquaminder.feature_weather.domain.model.getColorByIcon
import com.example.aquaminder.feature_weather.domain.model.getHumidityFormatted
import com.example.aquaminder.feature_weather.domain.model.getRainProbFormatted
import com.example.aquaminder.feature_weather.domain.model.getTemperatureFormatted
import com.example.aquaminder.feature_weather.domain.model.getTimeFormatted
import com.example.aquaminder.feature_weather.utils.WeatherUtils.setWeatherIcon

class HourlyWeatherAdapter(
    private var items: List<HourlyWeatherDomainModel>
) : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val binding = ItemHourlyWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HourlyWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<HourlyWeatherDomainModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class HourlyWeatherViewHolder(private val binding: ItemHourlyWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HourlyWeatherDomainModel) {

            binding.tvTime.text = item.getTimeFormatted()
            binding.tvTemp.text = item.getTemperatureFormatted()
            binding.tvHumidity.text = item.getHumidityFormatted()
            binding.tvRainProb.text = item.getRainProbFormatted()
            binding.wvIcon.setWeatherIcon(item.icon, isSmall = true)

            val colorRes = item.getColorByIcon()

            binding.viewAccent.setBackgroundColor(
                ContextCompat.getColor(binding.root.context, colorRes)
            )
        }
    }
}
