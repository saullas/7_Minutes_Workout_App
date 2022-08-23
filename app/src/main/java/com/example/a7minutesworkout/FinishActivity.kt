package com.example.a7minutesworkout

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.a7minutesworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class FinishActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    var binding : ActivityFinishBinding? = null

    private var textToSpeech : TextToSpeech? = null
    private var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        textToSpeech = TextToSpeech(this, this)
        val historyDao = (application as WorkoutApp).db.historyDao()

        binding?.ivFireworks?.let { Glide.with(this).load(R.drawable.fireworks).into(it) }

        makeSound(R.raw.cheering)
        Handler(Looper.getMainLooper()).postDelayed({
            speak("Congratualtions! You finished all the exercises.")
        }, 700)

        binding?.btnFinish?.setOnClickListener {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        addToHistory(historyDao)
    }

    private fun addToHistory(historyDao : HistoryDao) {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date = sdf.format(c.time)

        lifecycleScope.launch {
            historyDao.insert(HistoryEntity(date))
            Log.e("INSERT HISTORY: ", date)
        }
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
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
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
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        mediaPlayer?.stop()
        binding = null
    }
}