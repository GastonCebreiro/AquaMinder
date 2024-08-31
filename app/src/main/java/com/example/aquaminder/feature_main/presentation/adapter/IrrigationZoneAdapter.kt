package com.example.aquaminder.feature_main.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aquaminder.databinding.ItemIrrigationZoneBinding
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneUiModel

class IrrigationZoneAdapter(
    private val irrigationZoneList: List<IrrigationZoneUiModel>,
    private val onClick: (IrrigationZoneUiModel) -> Unit
) : RecyclerView.Adapter<IrrigationZoneAdapter.IrrigationZoneHolder>() {

    inner class IrrigationZoneHolder(private val binding: ItemIrrigationZoneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = irrigationZoneList[position]
                    onClick(clickedItem)
                }
            }
        }

        fun bind(item: IrrigationZoneUiModel) {
            binding.ivIZLogo.setImageResource(item.logoId)
            binding.tvIZName.text = item.name
            binding.tvIZId.text = item.uuid
            binding.clIZ.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    item.colorId
                )
            )
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IrrigationZoneHolder {
        val binding = ItemIrrigationZoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IrrigationZoneHolder(binding)
    }

    override fun onBindViewHolder(holder: IrrigationZoneHolder, position: Int) {
        val item = irrigationZoneList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return irrigationZoneList.size
    }

}