package com.example.carmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carmanager.R
import com.example.carmanager.`interface`.OnClickListener
import com.example.carmanager.databinding.ItemSlotBinding
import com.example.carmanager.model.Slot

class SlotAdapter(private val isFromHistory: Boolean, val listener: OnClickListener): ListAdapter<Slot, SlotAdapter.ViewHolder>(SlotDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener, isFromHistory)
    }

    class ViewHolder(val binding: ItemSlotBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Slot,
            listener: OnClickListener,
            fromHistory: Boolean
        ) {
            with(binding) {
                root.setOnClickListener {
                    listener.onClick(item.name)
                }
                viewModel = item
                executePendingBindings()
            }
            if(fromHistory) {
                binding.tvStatus.text = "XemCT"
            } else {
                binding.tvNumberOrder.setTextColor(binding.root.context.resources.getColor(R.color.white))
                binding.tvStatus.setTextColor(binding.root.context.resources.getColor(R.color.white))
                if(item.status) {
                    binding.lnContainer.setBackgroundColor(binding.root.context.resources.getColor(R.color.red))
                } else {
                    binding.lnContainer.setBackgroundColor(binding.root.context.resources.getColor(R.color.green))
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSlotBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class SlotDiffCallback: DiffUtil.ItemCallback<Slot>() {
    override fun areItemsTheSame(oldItem: Slot, newItem: Slot): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Slot, newItem: Slot): Boolean {
        return oldItem.status == newItem.status
    }

}