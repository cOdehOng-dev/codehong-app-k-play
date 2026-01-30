package com.codehong.app.kplay.ui.performance.list

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.base.BaseViewModel
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformanceListViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<PerformanceListEvent, PerformanceListState, PerformanceListEffect>(application) {

    override fun createInitialState(): PerformanceListState = PerformanceListState()

    override fun handleEvents(event: PerformanceListEvent) {
        when (event) {
            is PerformanceListEvent.OnPerformanceClick -> {
                setEffect { PerformanceListEffect.NavigateToDetail(event.item) }
            }
        }
    }

    fun callPerformanceListApi(
        signGuCode: String,
        signGuCodeSub: String
    ) {
        viewModelScope.launch {
            performanceUseCase.getPerformanceList(
                service = BuildConfig.KOKOR_CLIENT_ID,
                startDate = "20260101",
                endDate = "20260630",
                currentPage = "1",
                rowsPerPage = "100",
                signGuCode = signGuCode,
                signGuCodeSub = signGuCodeSub
            ).collect {
                setState { copy(performanceList = it ?: emptyList()) }
            }
        }
    }

}