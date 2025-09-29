package com.example.aquaminder.feature_configuration.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aquaminder.databinding.ItemTimeChipBinding
import com.google.android.material.chip.Chip

class TimeAdapter(
    private val times: MutableList<String>,
    private val onRemove: (String) -> Unit,
    private val onEdit: (String) -> Unit
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(val binding: ItemTimeChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val chip: Chip = binding.chipTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = ItemTimeChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        val time = times[position]
        holder.chip.text = time

        // Remove (close icon)
        holder.chip.setOnCloseIconClickListener {
            val removed = times.removeAt(position)
            notifyItemRemoved(position)
            onRemove(removed)
        }

        // Edit (tap chip)
        holder.chip.setOnClickListener {
            onEdit(time)
        }
    }

    override fun getItemCount(): Int = times.size

    fun addTime(newTime: String) {
        times.add(newTime)
        notifyItemInserted(times.lastIndex)
    }

    fun updateTime(oldTime: String, newTime: String) {
        val index = times.indexOf(oldTime)
        if (index != -1) {
            times[index] = newTime
            notifyItemChanged(index)
        }
    }
}