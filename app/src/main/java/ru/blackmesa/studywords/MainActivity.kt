package ru.blackmesa.studywords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.navigation.fragment.NavHostFragment
import org.koin.android.ext.android.inject
import ru.blackmesa.studywords.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val textToSpeech: TextToSpeech by inject()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //To initialise, or first phrase lost
        textToSpeech.speak(
            " ",
            TextToSpeech.QUEUE_FLUSH,
            null,
            this.hashCode().toString()
        )

    }
}