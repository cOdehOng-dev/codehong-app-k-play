package com.codehong.app.kplay.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.library.architecture.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<MainEvent, MainState, MainEffect>(application) {

    override fun createInitialState(): MainState = MainState()

    override fun handleEvents(event: MainEvent) {
        when (event) {
            is MainEvent.OnPerformanceClick -> {
                setEffect { MainEffect.NavigateToDetail(event.item) }
            }
        }
    }

    fun callPerformanceListApi() {
        viewModelScope.launch {
            performanceUseCase.getPerformanceList(
                service = BuildConfig.KOKOR_CLIENT_ID,
                startDate = "20260101",
                endDate = "20260630",
                currentPage = "1",
                rowsPerPage = "100"
            ).collect {
                setState { copy(performanceList = it ?: emptyList()) }
            }
        }
    }
}