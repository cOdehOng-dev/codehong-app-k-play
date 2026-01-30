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
                Log.d(TAG, "Category clicked: ${event.cateCode.displayName} (code: ${event.cateCode.code})")
                setEffect { LoungeEffect.NavigateToCategory(event.cateCode) }
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
}
