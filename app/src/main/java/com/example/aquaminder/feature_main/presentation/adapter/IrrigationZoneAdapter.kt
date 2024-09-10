package com.example.aquaminder.feature_main.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aquaminder.R
import com.example.aquaminder.core.utils.DialogUtils
import com.example.aquaminder.databinding.ItemIrrigationZoneBinding
import com.example.aquaminder.feature_main.presentation.model.IrrigationZoneDomainModel
import com.google.android.material.snackbar.Snackbar

class IrrigationZoneAdapter(
    private val irrigationZoneList: MutableList<IrrigationZoneDomainModel>,
    private val onClick: (IrrigationZoneDomainModel) -> Unit,
    private val onShareId: (String) -> Unit,
) : RecyclerView.Adapter<IrrigationZoneAdapter.IrrigationZoneHolder>() {

    inner class IrrigationZoneHolder(private val binding: ItemIrrigationZoneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clShare.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val shareItem = irrigationZoneList[position]
                    onShareId(shareItem.uuid)
                }
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = irrigationZoneList[position]
                    onClick(clickedItem)
                }
            }
        }

        fun bind(item: IrrigationZoneDomainModel) {
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
        val binding =
            ItemIrrigationZoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IrrigationZoneHolder(binding)
    }

    override fun onBindViewHolder(holder: IrrigationZoneHolder, position: Int) {
        val item = irrigationZoneList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return irrigationZoneList.size
    }

    fun removeItem(context: Context, position: Int) {
        try {
            if (position >= 0 && position < irrigationZoneList.size) {
                irrigationZoneList.removeAt(position)
                notifyItemRemoved(position)
            } else {
                DialogUtils.showErrorDialog(
                    context = context,
                    titleText = context.getString(
                        R.string.fragment_irrigation_zones_delete_item_position_error
                    )
                )
            }
        } catch (e: Exception) {
            DialogUtils.showErrorDialog(
                context = context,
                titleText = e.message.toString()
            )
        }
    }

}