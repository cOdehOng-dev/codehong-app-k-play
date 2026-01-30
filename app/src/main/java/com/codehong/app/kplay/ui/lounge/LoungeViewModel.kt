package com.codehong.app.kplay.ui.lounge

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.base.BaseViewModel
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.app.util.DateUtil
import com.codehong.library.network.debug.TimberUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoungeViewModel"

@HiltViewModel
class LoungeViewModel @Inject constructor(
    private val application: Application,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<
    LoungeEvent,
    LoungeState,
    LoungeEffect
>(application) {

    override fun createInitialState(): LoungeState {
        return LoungeState(
            currentMonth = DateUtil.getCurrentMonth()
        )
    }

    override fun handleEvents(event: LoungeEvent) {
        when (event) {
            is LoungeEvent.OnTabSelected -> {
                Log.d(TAG, "Tab selected: ${event.tab.title}")
                setState { copy(selectedTab = event.tab) }
            }
            is LoungeEvent.OnCategoryClick -> {
                Log.d(TAG, "Category clicked: ${event.genreCode.displayName} (code: ${event.genreCode.code})")
                setEffect { LoungeEffect.NavigateToCategory(event.genreCode) }
            }
            is LoungeEvent.OnRankTabSelected -> {
                Log.d(TAG, "Rank tab selected: ${event.rankTab.title}")
                setState { copy(selectedRankTab = event.rankTab) }
            }
            is LoungeEvent.OnRankItemClick -> {
                Log.d(TAG, "Rank item clicked - performanceId: ${event.item.performanceId}")
                event.item.performanceId?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnRefreshNearbyClick -> {
                Log.d(TAG, "Refresh nearby clicked")
                setEffect { LoungeEffect.RequestLocationPermission }
            }
            is LoungeEvent.OnNearbyItemClick -> {
                Log.d(TAG, "Nearby item clicked - id: ${event.item.id}")
                event.item.id?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnSignGuCodeUpdated -> {
                Log.d(TAG, "SignGuCode updated: ${event.signGuCode.displayName}")
                setState { copy(selectedSignGuCode = event.signGuCode) }
                callMyAreaListApi(event.signGuCode.code)
            }
        }
    }

    fun callRankList() {
        val startDate = DateUtil.getCurrentMonthFirstDay()
        val endDate = DateUtil.getCurrentMonthLastDay()

        viewModelScope.launch {
            performanceUseCase.getRankList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate
            ).collect { rankList ->
                TimberUtil.d("Rank list size: ${rankList?.size ?: 0}")
                rankList?.let { list ->
                    setState {
                        copy(
                            rankList = list,
                            currentMonth = DateUtil.getCurrentMonth()
                        )
                    }
                }
            }
        }
    }

    fun callMyAreaListApi(myAreaCode: String = state.value.selectedSignGuCode.code) {
        val startDate = DateUtil.getToday()
        val endDate = DateUtil.getOneMonthLater()

        viewModelScope.launch {
            performanceUseCase.getPerformanceList(
                service = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                currentPage = "1",
                rowsPerPage = "10",
                signGuCode = myAreaCode,
            ).collect { list ->
                TimberUtil.d("My area list size: ${list?.size ?: 0}")
                list?.let {
                    setState { copy(myAreaList = it) }
                }
            }
        }
    }
}
