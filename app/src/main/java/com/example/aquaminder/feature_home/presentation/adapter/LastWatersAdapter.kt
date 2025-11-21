package com.example.aquaminder.feature_home.presentation.adapter

import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.example.aquaminder.R
import com.example.aquaminder.databinding.ItemLastWaterBinding
import com.example.aquaminder.feature_configuration.presentation.fragments.LastWatersDomainModel
import android.view.ViewGroup
import com.example.aquaminder.feature_configuration.presentation.fragments.getFormattedDate

class LastWatersAdapter(
    private val items: List<LastWatersDomainModel>
) : RecyclerView.Adapter<LastWatersAdapter.LastWaterViewHolder>() {

    inner class LastWaterViewHolder(private val binding: ItemLastWaterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LastWatersDomainModel) {
            binding.tvTime.text = item.time
            binding.tvDate.text = item.getFormattedDate()

            if (item.isSkipped) {
                // Saltado
                binding.tvStatus.text = binding.root.context.getString(R.string.home_fragment_last_water_skipped)
                binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.orange))
                binding.ivIcon.setImageResource(R.drawable.ic_skipped)
                binding.root.background = ContextCompat.getDrawable(binding.root.context, R.drawable.background_last_water_skipped)
            } else {
                // Regado
                binding.tvStatus.text =
                    binding.root.context.getString(R.string.home_fragment_last_water_irrigated)
                binding.tvStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.light_blue))
                binding.ivIcon.setImageResource(R.drawable.ic_check)
                binding.root.background = ContextCompat.getDrawable(binding.root.context, R.drawable.background_last_water)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastWaterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLastWaterBinding.inflate(inflater, parent, false)
        return LastWaterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LastWaterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
