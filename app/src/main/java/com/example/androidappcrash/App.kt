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

/**
 * The Application class initializes the backtraceClient and sets it up
 */
class App : Application() {

    lateinit var backtraceClient: BacktraceClient

    override fun onCreate() {
        super.onCreate()

        // The uri endpoint, change the strings in strings.xml with your universe and token
        val uri = "https://submit.backtrace.io/${getString(R.string.backtrace_universe)}/${getString(R.string.backtrace_token)}/json"
        val credentials = BacktraceCredentials(uri)

        // Local db setup, required for native error reporting
        val dbPath = filesDir.absolutePath + "/backtrace"
        val settings = BacktraceDatabaseSettings(dbPath)
        settings.maxRecordCount = 100
        settings.maxDatabaseSize = 100
        settings.retryBehavior = RetryBehavior.ByInterval
        settings.isAutoSendMode = true
        settings.retryOrder = RetryOrder.Queue

        val database = BacktraceDatabase(this, settings)
        backtraceClient = BacktraceClient(this, credentials, database)

        // Enable native crash reporting automatically sets up signal handler and report crash to server
        database.setupNativeIntegration(backtraceClient, credentials)
        BacktraceExceptionHandler.enable(backtraceClient)
        backtraceClient.enableNativeIntegration()
        backtraceClient.metrics.enable(BacktraceMetricsSettings(credentials))

        // Enable breadcrumbs
        backtraceClient.enableBreadcrumbs(this)
    }
}