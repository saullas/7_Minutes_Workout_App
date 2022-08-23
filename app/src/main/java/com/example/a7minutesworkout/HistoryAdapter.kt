package com.example.a7minutesworkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items: ArrayList<HistoryEntity>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){

    class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llHistoryItem = binding.llHistoryItem
        val tvDate = binding.tvDate
        val tvPosition = binding.tvPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pastWorkout: HistoryEntity = items[position]
        holder.tvPosition.text = (position + 1).toString()
        holder.tvDate.text = pastWorkout.date

        if (position % 2 == 0) {
            holder.llHistoryItem.setBackgroundResource(R.drawable.history_item_background_light)
        } else {
            holder.llHistoryItem.setBackgroundResource(R.drawable.history_item_background_dark)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}