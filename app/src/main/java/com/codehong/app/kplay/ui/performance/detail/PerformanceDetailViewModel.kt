package com.codehong.app.kplay.ui.performance.detail

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.library.architecture.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformanceDetailViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<PerformanceDetailEvent, PerformanceDetailState, PerformanceDetailEffect>(application) {

    override fun createInitialState(): PerformanceDetailState = PerformanceDetailState()

    override fun handleEvents(event: PerformanceDetailEvent) {
        when (event) {
            is PerformanceDetailEvent.OnBookingClick -> {

                val ticketSiteList = state.value.performanceDetail?.ticketSiteList
                if (!ticketSiteList.isNullOrEmpty()) {
                    setEffect { PerformanceDetailEffect.OpenBookingPage(ticketSiteList) }
                }
            }
            is PerformanceDetailEvent.OnBackClick -> {
                setEffect { PerformanceDetailEffect.NavigateBack }
            }
        }
    }

    fun callPerformanceDetailApi(performanceId: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            performanceUseCase.getPerformanceDetail(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                id = performanceId
            ).collect { result ->
                setState {
                    copy(
                        performanceDetail = result?.firstOrNull(),
                        isLoading = false
                    )
                }
            }
        }
    }
}
