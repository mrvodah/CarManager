package com.example.carmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.carmanager.databinding.ItemTimeBinding
import com.example.carmanager.model.Time

class TimeAdapter: ListAdapter<Time, TimeAdapter.ViewHolder>(TimeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(val binding: ItemTimeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Time) {
            with(binding) {
                viewModel = item
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTimeBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class TimeDiffCallback: DiffUtil.ItemCallback<Time>() {
    override fun areItemsTheSame(oldItem: Time, newItem: Time): Boolean {
        return oldItem.license_plate == newItem.license_plate
    }

    override fun areContentsTheSame(oldItem: Time, newItem: Time): Boolean {
        return oldItem == newItem
    }

}