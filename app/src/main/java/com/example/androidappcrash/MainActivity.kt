package com.example.androidappcrash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidappcrash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI()

        binding.outOfBound.setOnClickListener {
            doCrash(1)
        }

        binding.outOfMemory.setOnClickListener {
            doCrash(2)
        }

        binding.nullptr.setOnClickListener {
            doCrash(3)
        }
    }

    /**
     * A native method that is implemented by th₹e 'androidappcrash' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun doCrash(type: Int)

    companion object {
        // Used to load the 'androidappcrash' library on application startup.
        init {
            System.loadLibrary("androidappcrash")
        }
    }
}