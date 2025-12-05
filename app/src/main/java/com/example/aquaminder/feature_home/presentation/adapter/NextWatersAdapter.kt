package com.example.aquaminder.feature_home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aquaminder.databinding.ItemNextWaterBinding
import com.example.aquaminder.feature_configuration.domain.model.NextWatersDomainModel
import com.example.aquaminder.feature_configuration.domain.model.getFormattedDate

class NextWatersAdapter(
    private val items: List<NextWatersDomainModel>
) : RecyclerView.Adapter<NextWatersAdapter.NextWaterViewHolder>() {

    inner class NextWaterViewHolder(private val binding: ItemNextWaterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NextWatersDomainModel) {
            binding.tvTime.text = item.time
            binding.tvDate.text = item.getFormattedDate()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextWaterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNextWaterBinding.inflate(inflater, parent, false)
        return NextWaterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NextWaterViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
