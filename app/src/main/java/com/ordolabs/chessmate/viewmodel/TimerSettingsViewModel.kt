package com.ordolabs.chessmate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ordolabs.chessmate.mapper.toDomain
import com.ordolabs.chessmate.mapper.toPresentation
import com.ordolabs.chessmate.model.presentation.TimerSettingsPresentation
import com.ordolabs.chessmate.viewmodel.base.BaseViewModel
import com.ordolabs.domain.usecase.datastore.GetTimerSettingsUseCase
import com.ordolabs.domain.usecase.datastore.SetTimerSettingsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TimerSettingsViewModel(
    private val getTimerSettingsUseCase: GetTimerSettingsUseCase,
    private val setTimerSettingsUseCase: SetTimerSettingsUseCase
) : BaseViewModel() {

    val settings: LiveData<TimerSettingsPresentation>
        get() = _settings

    private var _settings = MutableLiveData<TimerSettingsPresentation>()

    fun getTimerSettings(): LiveData<TimerSettingsPresentation> {
        viewModelScope.launch {
            getTimerSettingsUseCase.invoke(Unit).collect {
                _settings.value = it.toPresentation()
            }
        }
        return _settings
    }

    fun setTimerSettings(settings: TimerSettingsPresentation) {
        viewModelScope.launch {
            setTimerSettingsUseCase.invoke(settings.toDomain())
        }
    }

    fun parseTimerSettingsLimit(settings: TimerSettingsPresentation): Long {
        val minutes = settings.limitMinutes * 60 * 1000L
        val seconds = settings.limitSeconds * 1000L
        return minutes + seconds
    }
}