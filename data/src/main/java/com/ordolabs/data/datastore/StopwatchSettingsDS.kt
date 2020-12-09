package com.ordolabs.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.ordolabs.data.R
import com.ordolabs.domain.model.DSStopwatchSettingsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StopwatchSettingsDS(context: Context) {

    fun getStopwatchSettings(): Flow<DSStopwatchSettingsModel> = dataStore.data.map {
        val minutes = it[keyLimitMinutes] ?: defaultLimitMinutes
        val seconds = it[keyLimitSeconds] ?: defaultLimitSeconds
        val player1 = it[keyPlayer1] ?: defaultPlayer1
        val player2 = it[keyPlayer2] ?: defaultPlayer2
        DSStopwatchSettingsModel(minutes, seconds, player1, player2)
    }

    suspend fun setStopwatchSettings(settings: DSStopwatchSettingsModel) = dataStore.edit {
        it[keyLimitMinutes] = settings.limitMinutes
        it[keyLimitSeconds] = settings.limitSeconds
        it[keyPlayer1] = settings.player1
        it[keyPlayer2] = settings.player2
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = DATASTORE_NAME
    )

    private val keyLimitMinutes = preferencesKey<Int>(PK_LIMIT_MINUTES)
    private val keyLimitSeconds = preferencesKey<Int>(PK_LIMIT_SECONDS)
    private val keyPlayer1 = preferencesKey<String>(PK_PLAYER_1)
    private val keyPlayer2 = preferencesKey<String>(PK_PLAYER_2)

    private val defaultLimitMinutes = context.resources.getInteger(
        R.integer.data_ds_stopwatch_settings_limit_minutes_default
    )
    private val defaultLimitSeconds = context.resources.getInteger(
        R.integer.data_ds_stopwatch_settings_limit_seconds_default
    )
    private val defaultPlayer1 = context.getString(
        R.string.data_ds_stopwatch_settings_player1_default
    )
    private val defaultPlayer2 = context.getString(
        R.string.data_ds_stopwatch_settings_player2_default
    )

    companion object {
        private const val DATASTORE_NAME = "stopwatch_settings"

        private const val PK_LIMIT_MINUTES = "limit_minutes"
        private const val PK_LIMIT_SECONDS = "limit_seconds"
        private const val PK_PLAYER_1 = "player_1"
        private const val PK_PLAYER_2 = "player_2"
    }
}