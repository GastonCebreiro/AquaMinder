package com.example.aquaminder.feature_new_irrigation_zone.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aquaminder.databinding.ItemLogoBinding

class LogoViewPagerAdapter(
    private val images: List<Int>,
    private val colors: List<Int>
) : RecyclerView.Adapter<LogoViewPagerAdapter.LogoViewHolder>() {

    class LogoViewHolder(val binding: ItemLogoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogoViewHolder {
        val binding = ItemLogoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogoViewHolder, position: Int) {
        holder.binding.ivIZLogo.setImageResource(images[position])
        holder.binding.clLogo.setBackgroundColor(
            ContextCompat.getColor(
                holder.binding.root.context,
                colors[position]
            )
        )
    }

    override fun getItemCount(): Int = images.size
}
