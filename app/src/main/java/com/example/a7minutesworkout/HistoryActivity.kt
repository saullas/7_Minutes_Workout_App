package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    var binding : ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val historyDao = (application as WorkoutApp).db.historyDao()

        getPastWorkouts(historyDao)
    }

    private fun getPastWorkouts(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.getAll().collect { pastWorkouts ->
                if (pastWorkouts.isNotEmpty()) {
                    binding?.tvNoData?.visibility = View.GONE
                    binding?.tvTitle?.visibility = View.VISIBLE
                    binding?.rvPastWorkouts?.visibility = View.VISIBLE
                    binding?.rvPastWorkouts?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    binding?.rvPastWorkouts?.adapter = HistoryAdapter(ArrayList(pastWorkouts))
                } else {
                    binding?.tvNoData?.visibility = View.VISIBLE
                    binding?.tvTitle?.visibility = View.GONE
                    binding?.rvPastWorkouts?.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}