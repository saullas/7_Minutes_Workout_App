package com.example.a7minutesworkout

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.a7minutesworkout.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    var binding : ActivityBmiactivityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.rgUnit?.setOnCheckedChangeListener { radioGroup, checkedId ->
            if (checkedId == R.id.rbMetricUnits) {
                binding?.tilHeight?.visibility = View.VISIBLE
                binding?.tilWeight?.visibility = View.VISIBLE
                binding?.tilHeightInch?.visibility = View.GONE
                binding?.tilHeightFeet?.visibility = View.GONE
                binding?.tilWeightPounds?.visibility = View.GONE

                binding?.etHeightInch?.text!!.clear()
                binding?.etHeightFeet?.text!!.clear()
                binding?.etWeightPounds?.text!!.clear()
            } else {
                binding?.tilHeightInch?.visibility = View.VISIBLE
                binding?.tilHeightFeet?.visibility = View.VISIBLE
                binding?.tilWeightPounds?.visibility = View.VISIBLE
                binding?.tilHeight?.visibility = View.GONE
                binding?.tilWeight?.visibility = View.GONE

                binding?.etHeight?.text!!.clear()
                binding?.etWeight?.text!!.clear()
            }
        }

        binding?.btnCalculate?.setOnClickListener {
            if (isInputDataValid()) {
                var bmi = 0f
                if (binding?.rbMetricUnits!!.isChecked()) {
                    val height = binding?.etHeight?.text.toString().toFloat() / 100
                    val weight = binding?.etWeight?.text.toString().toFloat()
                    bmi = BigDecimal((weight / (height * height)).toDouble()).setScale(2, RoundingMode.HALF_EVEN).toFloat()
                } else {
                    val heightFeet = binding?.etHeightFeet?.text.toString().toFloat()
                    val heightInch = binding?.etHeightInch?.text.toString().toFloat()
                    val height = heightFeet * 12 + heightInch
                    val weight = binding?.etWeightPounds?.text.toString().toFloat()
                    bmi = BigDecimal((703 * (weight / (height * height))).toDouble()).setScale(2, RoundingMode.HALF_EVEN).toFloat()
                }


                binding?.llBMIDescription?.visibility = View.VISIBLE
                binding?.tvBMIValue?.text = bmi.toString()

                when (bmi) {
                    in 0f..15f -> {
                        binding?.tvBMIClass?.text = "Very severely underweight!"
                        binding?.tvBMIDescription?.text = "You really need to act and eat more!"
                        binding?.tvBMIClass?.setTextColor(ContextCompat.getColor(this, R.color.bmi_extremely_bad))
                    }
                    in 15f..16f -> {
                        binding?.tvBMIClass?.text = "Severely underweight"
                        binding?.tvBMIDescription?.text = "You really need to eat more!"
                        binding?.tvBMIClass?.setTextColor(ContextCompat.getColor(this, R.color.bmi_very_bad))
                    }
                    in 16f..18.5f -> {
                        binding?.tvBMIClass?.text = "Underweight"
                        binding?.tvBMIDescription?.text = "Almost normal, but you should still eat more."
                        binding?.tvBMIClass?.setTextColor(ContextCompat.getColor(this, R.color.bmi_bad))
                    }
                    in 18.5f..25f -> {
                        binding?.tvBMIClass?.text = "Normal"
                        binding?.tvBMIDescription?.text = "Congrats, you are in good shape! Keep it that way!"
                        binding?.tvBMIClass?.setTextColor(ContextCompat.getColor(this, R.color.bmi_good))
                    }
                    in 25f..30f -> {
                        binding?.tvBMIClass?.text = "Overweight"
                        binding?.tvBMIDescription?.text = "You really need to eat less and/or better and exercise more!"
                        binding?.tvBMIClass?.setTextColor(ContextCompat.getColor(this, R.color.bmi_bad))
                    }
                    in 30f..35f -> {
                        binding?.tvBMIClass?.text = "Obese Class I (Moderately obese)"
                        binding?.tvBMIDescription?.text = "You really need to eat less and/or better and exercise more!"
                        binding?.tvBMIClass?.setTextColor(ContextCompat.getColor(this, R.color.bmi_very_bad))
                    }
                    in 35f..40f -> {
                        binding?.tvBMIClass?.text = "Obese Class II (Severely obese)"
                        binding?.tvBMIDescription?.text = "You are in a bad shape and in a dangerous condition! You really need to eat less and better and exercise more!"
                        binding?.tvBMIClass?.setTextColor(ContextCompat.getColor(this, R.color.bmi_extremely_bad))
                    }
                    else -> {
                        binding?.tvBMIClass?.text = "Obese Class III (Very Severely obese)"
                        binding?.tvBMIDescription?.text = "You are in a very bad shape and in a very dangerous condition! You really need to act ! Eat less and better and exercise more!"
                        binding?.tvBMIClass?.setTextColor(ContextCompat.getColor(this, R.color.bmi_extremely_bad))
                    }
                }
            } else {
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isInputDataValid() : Boolean {
        if (binding?.rbMetricUnits!!.isChecked()) {
            return binding?.etWeight?.text.toString().isNotEmpty() and binding?.etHeight?.text.toString().isNotEmpty()
        } else {
            return binding?.etWeightPounds?.text.toString().isNotEmpty() and binding?.etHeightFeet?.text.toString().isNotEmpty() and binding?.etHeightInch?.text.toString().isNotEmpty()
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}