package com.example.androidappcrash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import backtraceio.library.models.json.BacktraceReport
import backtraceio.library.models.types.BacktraceResultStatus
import com.example.androidappcrash.databinding.ActivityMainBinding

// Token ce6cdc1b122528a5986ae888bc26fa59e0047c895a57073570f15d2f35ccc168

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.kotlinCrash.setOnClickListener {
            testKotlinCrash("Crash 1")
        }
    }

    fun testKotlinCrash(msg: String) {
        try {
            throw Exception("Testing, crash from kotlin: $msg")
        }
        catch (e: Exception) {
            val app = applicationContext as App
            app.backtraceClient.send(BacktraceReport(e)) { result ->

                if (result.status == BacktraceResultStatus.Ok) {
                    Log.d("Backtrace", "Crash sent successfully")
                } else {
                    Log.e("Backtrace", "Failed: ${result.message}")
                }
            }
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