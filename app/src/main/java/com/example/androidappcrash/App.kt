package com.example.androidappcrash

import android.app.Application
import backtraceio.library.BacktraceClient
import backtraceio.library.BacktraceCredentials
import backtraceio.library.BacktraceDatabase
import backtraceio.library.enums.database.RetryBehavior
import backtraceio.library.enums.database.RetryOrder
import backtraceio.library.models.BacktraceExceptionHandler
import backtraceio.library.models.BacktraceMetricsSettings
import backtraceio.library.models.database.BacktraceDatabaseSettings


class App : Application() {

    lateinit var backtraceClient: BacktraceClient

    override fun onCreate() {
        super.onCreate()

        val credentials = BacktraceCredentials(
            "https://submit.backtrace.io/nitinkhurana/ce6cdc1b122528a5986ae888bc26fa59e0047c895a57073570f15d2f35ccc168/json"
        )

        val dbPath = filesDir.absolutePath + "/backtrace"
        val settings = BacktraceDatabaseSettings(dbPath)
        settings.maxRecordCount = 100
        settings.maxDatabaseSize = 100
        settings.retryBehavior = RetryBehavior.ByInterval
        settings.isAutoSendMode = true
        settings.retryOrder = RetryOrder.Queue

        val database = BacktraceDatabase(this, settings)
        backtraceClient = BacktraceClient(this, credentials, database)

        database.setupNativeIntegration(backtraceClient, credentials)
        BacktraceExceptionHandler.enable(backtraceClient)
        backtraceClient.enableNativeIntegration()
        backtraceClient.metrics.enable(BacktraceMetricsSettings(credentials))
    }
}