package com.example.a7minutesworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<Exercise>) :
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemExerciseStatusBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var exercise: Exercise = items[position]
        holder.tvItem.text = exercise.id.toString()

        when {
            exercise.isSelected -> {
//                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.exercise_selected)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            exercise.isCompleted -> {
//                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.exercise_completed)
                holder.tvItem.setTextColor(Color.parseColor("#252525"))
            }
            else -> {

            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}