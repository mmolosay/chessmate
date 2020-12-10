package com.ordolabs.chessmate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ordolabs.chessmate.mapper.toDomain
import com.ordolabs.chessmate.mapper.toPresentation
import com.ordolabs.chessmate.model.TimerSettingsPresentation
import com.ordolabs.chessmate.viewmodel.base.BaseViewModel
import com.ordolabs.domain.usecase.datastore.GetTimerSettingsUseCase
import com.ordolabs.domain.usecase.datastore.SetTimerSettingsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TimerSettingsViewModel(
    private val getTimerSettingsUseCase: GetTimerSettingsUseCase,
    private val setTimerSettingsUseCase: SetTimerSettingsUseCase
) : BaseViewModel() {

    fun getTimerSettings(): LiveData<TimerSettingsPresentation> {
        val livedata = MutableLiveData<TimerSettingsPresentation>()
        viewModelScope.launch {
            getTimerSettingsUseCase.invoke(Unit).collect {
                livedata.value = it.toPresentation()
            }
        }
        return livedata
    }

    fun setTimerSettings(settings: TimerSettingsPresentation) {
        viewModelScope.launch {
            setTimerSettingsUseCase.invoke(settings.toDomain())
        }
    }
}