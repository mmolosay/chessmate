package com.ordolabs.chessmate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ordolabs.chessmate.mapper.toDomain
import com.ordolabs.chessmate.mapper.toPresentation
import com.ordolabs.chessmate.model.StopwatchSettingsPresentation
import com.ordolabs.chessmate.viewmodel.base.BaseViewModel
import com.ordolabs.domain.usecase.datastore.GetStopwatchSettingsUseCase
import com.ordolabs.domain.usecase.datastore.SetStopwatchSettingsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StopwatchSettingsViewModel(
    private val getStopwatchSettingsUseCase: GetStopwatchSettingsUseCase,
    private val setStopwatchSettingsUseCase: SetStopwatchSettingsUseCase
) : BaseViewModel() {

    fun getStopwatchSettings(): LiveData<StopwatchSettingsPresentation> {
        val livedata = MutableLiveData<StopwatchSettingsPresentation>()
        viewModelScope.launch {
            getStopwatchSettingsUseCase.invoke(Unit).collect {
                livedata.value = it.toPresentation()
            }
        }
        return livedata
    }

    fun setStopwatchSettings(settings: StopwatchSettingsPresentation) {
        viewModelScope.launch {
            setStopwatchSettingsUseCase.invoke(settings.toDomain())
        }
    }
}