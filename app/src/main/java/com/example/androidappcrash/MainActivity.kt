package com.example.androidappcrash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import backtraceio.library.enums.BacktraceBreadcrumbType
import backtraceio.library.models.json.BacktraceReport
import backtraceio.library.models.types.BacktraceResultStatus
import com.example.androidappcrash.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /**
     * Reference to the application class
     */
    private lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = applicationContext as App

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sampleText.text = stringFromJNI() // Sample JNI setup test

        binding.outOfBound.setOnClickListener {
            app.backtraceClient.addBreadcrumb("User clicked the outOfBound crash button", BacktraceBreadcrumbType.LOG)
            doCrash(1)
        }

        binding.outOfMemory.setOnClickListener {
            app.backtraceClient.addBreadcrumb("User clicked the outOfMemory crash button", BacktraceBreadcrumbType.LOG)
            doCrash(2)
        }

        binding.nullptr.setOnClickListener {
            app.backtraceClient.addBreadcrumb("User clicked the nullptr crash button", BacktraceBreadcrumbType.LOG)
            doCrash(3)
        }

        binding.kotlinCrash.setOnClickListener {
            testKotlinCrash("Crash 1")
        }
    }

    /**
     * Sample function to test backtrace error reporting using kotlin
     * [msg] The sample error message that will be sent along the stacktrace to the server
     */
    fun testKotlinCrash(msg: String) {
        try {
            throw Exception("Testing, crash from kotlin: $msg")
        }
        catch (e: Exception) {
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
     * Sample function to test JNI working
     */
    external fun stringFromJNI(): String

    /**
     * Native function that triggers crashes based on the [type] sent
     */
    external fun doCrash(type: Int)

    companion object {
        // Used to load the 'androidappcrash' library on application startup.
        init {
            System.loadLibrary("androidappcrash") // SO library containing the native code
        }
    }
}