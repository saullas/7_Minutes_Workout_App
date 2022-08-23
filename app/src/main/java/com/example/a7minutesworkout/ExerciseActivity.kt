package com.example.a7minutesworkout

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.CustomDialogBinding
import java.util.*
import kotlin.random.Random.Default.nextInt


class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    var binding : ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restTimeProgress = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseTimeProgress = 0
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<Exercise>? = null
    private var currentExerciseIndex = -1 // index of current exercise
    private var workoutMode : String = "Rest"

    private var textToSpeech : TextToSpeech? = null
    private var ttsSpeaking : Boolean = false
    private var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        textToSpeech = TextToSpeech(this, this)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        exerciseList = Constants.defaultExerciseList()
        binding?.pbExercise?.max = exerciseList!!.size
        binding?.pbExercise?.progress = exerciseProgress

        binding?.flDecreaseSeconds?.setOnClickListener { decreaseSeconds() }
        binding?.flIncreaseSeconds?.setOnClickListener { increaseSeconds() }

        setRestMode()
    }

    private fun increaseSeconds() {
        if (workoutMode == "Rest") {
            restTimeProgress = maxOf(0, restTimeProgress - 10)
        } else {
            exerciseTimeProgress = maxOf(0, exerciseProgress - 10)
        }
    }

    private fun decreaseSeconds() {
        if (workoutMode == "Rest") {
            restTimeProgress = minOf(Constants.REST_TIME, restTimeProgress + 10)
        } else {
            exerciseTimeProgress = minOf(Constants.EXERCISE_TIME, exerciseProgress + 10)
        }
    }

    private fun setExerciseMode() {
        workoutMode = "Exercise"
        exerciseTimer?.cancel() // If its already running
        exerciseTimeProgress = 0

        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.flRestView?.visibility = View.INVISIBLE

        speak("Start the exercise ${exerciseList?.get(currentExerciseIndex)!!.name}")

        binding?.progressBarExercise?.progress = exerciseTimeProgress
        binding?.tvTitls?.text = "EXERCISE: ${exerciseList?.get(currentExerciseIndex)!!.name}"
        binding?.ivExercise?.setImageResource(exerciseList!!.get(currentExerciseIndex).image)
        binding?.ivExercise?.let { fadeAnimation(it, 0.3f, 1.0f, 500) }
        binding?.tvNextExercise?.let { fadeAnimation(it, 1.0f, 0.0f, 500) }

        exerciseTimer = object: CountDownTimer((Constants.EXERCISE_TIME * 1000).toLong(), 1000) {
            override fun onTick(p0: Long) {

                if (!ttsSpeaking) {
                    if (Constants.EXERCISE_TIME - exerciseTimeProgress <= 3) {
                        makeSound(R.raw.clock_tick_timer_ending)
                    } else {
                        makeSound(R.raw.clock_tick)
                    }
                }

                exerciseTimeProgress++
                binding?.progressBarExercise?.progress = Constants.EXERCISE_TIME - exerciseTimeProgress
                binding?.tvTimerExercise?.text = (Constants.EXERCISE_TIME - exerciseTimeProgress).toString()
            }

            override fun onFinish() {
                makeSound(R.raw.clock_tick_workout_end)
                exerciseProgress++
                binding?.pbExercise?.progress = exerciseProgress

                exerciseList!![currentExerciseIndex].isCompleted = true
//                exerciseAdapter!!.notifyDataSetChanged()

                if (currentExerciseIndex < exerciseList!!.size - 1) {
                    setRestMode()
                } else {
//                    binding?.pbExercise?.progressTintList = ColorStateList.valueOf(Color.parseColor("#68A938"))
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }.start()
    }


    private fun setRestMode() {
        workoutMode = "Rest"
        restTimer?.cancel() // If its already running
        restTimeProgress = 0

        // Get a random speech text for end of exercise
        if (currentExerciseIndex > -1) {
            Handler(Looper.getMainLooper()).postDelayed({
                speak(Constants.restTTS()[nextInt(Constants.restTTS().size)])
            }, 500)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                speak("Get ready")
            }, 500)
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivExercise?.let { fadeAnimation(it, 0.3f, 0.3f, 500) }

        binding?.progressBar?.progress = restTimeProgress

        if (currentExerciseIndex < exerciseList!!.size - 1) {
            binding?.ivExercise?.setImageResource(exerciseList!![currentExerciseIndex+1].image)
            binding?.ivExercise?.visibility = View.VISIBLE
            binding?.tvTitls?.text = "GET READY IN"
            binding?.tvNextExercise?.visibility = View.VISIBLE
            binding?.tvNextExercise?.text = "Next exercise: ${exerciseList!![currentExerciseIndex+1].name.uppercase()}"
            binding?.tvNextExercise?.let { fadeAnimation(it, 1.0f, 1.0f, 800) }
        } else {
            binding?.ivExercise?.visibility = View.INVISIBLE
        }

        restTimer = object: CountDownTimer((Constants.REST_TIME * 1000).toLong(), 1000) {
            override fun onTick(p0: Long) {

                if (!ttsSpeaking) {
                    if (Constants.REST_TIME - restTimeProgress <= 3) {
                        makeSound(R.raw.clock_tick_timer_ending)
                    }
                    else if (Constants.REST_TIME - restTimeProgress <= (Constants.REST_TIME * 1000 - 1)) {
                        makeSound(R.raw.clock_tick)
                    }
                }

                restTimeProgress++
                binding?.progressBar?.progress = Constants.REST_TIME - restTimeProgress
                binding?.tvTimer?.text = (Constants.REST_TIME - restTimeProgress).toString()
            }

            override fun onFinish() {
                currentExerciseIndex++
                exerciseList!![currentExerciseIndex].isSelected = true
//                exerciseAdapter!!.notifyDataSetChanged()
                setExerciseMode()
            }
        }.start()
    }

    private fun fadeAnimation(view: View, from: Float, to: Float, duration: Long) {
        val a: Animation = AlphaAnimation(from, to)
        a.duration = duration
        a.fillAfter = true
        view.startAnimation(a)
    }

    private fun speak(text: String) {
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun makeSound(soundResourceString: Int) {
        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.a7minutesworkout/$soundResourceString"
            )
            mediaPlayer = MediaPlayer.create(applicationContext, soundURI)
            mediaPlayer?.setOnCompletionListener { mp -> mp.reset() }
            mediaPlayer?.isLooping = false
            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e("SOUND", e.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        restTimer?.cancel()
        exerciseTimer?.cancel()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        mediaPlayer?.stop()
        restTimeProgress = 0
        exerciseTimeProgress = 0
        currentExerciseIndex = -1
        binding = null
    }

    // For TTS
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Setting the language for tts
            val result = textToSpeech?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported!")
            } else {
                Log.e("TTS", "Initialization Successful!")
            }

            val speechListener = object : UtteranceProgressListener() {
                override fun onStart(p0: String?) {
                    ttsSpeaking = true
                }
                override fun onDone(p0: String?) {
                    ttsSpeaking = false
                }
                override fun onError(p0: String?) {
                    ttsSpeaking = false
                }
            }

            textToSpeech?.setOnUtteranceProgressListener(speechListener)
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    override fun onBackPressed() {
        customDialogForBackButton()
//        super.onBackPressed()
    }

    private fun customDialogForBackButton() {
        var customDialog = Dialog(this)
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnCustomDialogYes.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//            finish()
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnCustomDialogNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
}