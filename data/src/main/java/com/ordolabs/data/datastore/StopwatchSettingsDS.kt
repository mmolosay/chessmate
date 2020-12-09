package com.ordolabs.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.ordolabs.domain.model.DSStopwatchSettingsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StopwatchSettingsDS(private val context: Context) {

    fun getStopwatchSettings(): Flow<DSStopwatchSettingsModel> = dataStore.data.map {
        val minutes = it[keyLimitMinutes] ?: DEFAULT_LIMIT_MINUTES
        val seconds = it[keyLimitSeconds] ?: DEFAULT_LIMIT_SECONDS
        DSStopwatchSettingsModel(minutes, seconds)
    }

    suspend fun setStopwatchSettings(settings: DSStopwatchSettingsModel) = dataStore.edit {
        it[keyLimitMinutes] = settings.limitMinutes
        it[keyLimitSeconds] = settings.limitSeconds
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = DATASTORE_NAME
    )

    private val keyLimitMinutes = preferencesKey<Int>(PK_LIMIT_MINUTES)
    private val keyLimitSeconds = preferencesKey<Int>(PK_LIMIT_SECONDS)

    companion object {
        private const val DATASTORE_NAME = "stopwatch_settings"

        private const val PK_LIMIT_MINUTES = "limit_minutes"
        private const val PK_LIMIT_SECONDS = "limit_seconds"

        private const val DEFAULT_LIMIT_MINUTES = 1
        private const val DEFAULT_LIMIT_SECONDS = 30
    }
}