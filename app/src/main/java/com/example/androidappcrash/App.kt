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


        val uri = "https://submit.backtrace.io/${getString(R.string.backtrace_universe)}/${getString(R.string.backtrace_token)}/json"
        val credentials = BacktraceCredentials(uri)

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
        backtraceClient.enableBreadcrumbs(this)
    }
}