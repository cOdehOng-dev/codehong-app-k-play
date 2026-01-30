package com.codehong.app.kplay.ui.lounge

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.base.BaseViewModel
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.app.kplay.util.DateUtil
import com.codehong.library.network.debug.TimberUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoungeViewModel"

@HiltViewModel
class LoungeViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase
) : BaseViewModel<LoungeEvent, LoungeState, LoungeEffect>(application) {

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
            is LoungeEvent.OnGenreTabSelected -> {
                Log.d(TAG, "Genre tab selected: ${event.genreCode.displayName}")
                setState { copy(selectedGenreTab = event.genreCode) }
                callGenreRankList(event.genreCode.code)
            }
            is LoungeEvent.OnGenreRankItemClick -> {
                Log.d(TAG, "Genre rank item clicked - performanceId: ${event.item.performanceId}")
                event.item.performanceId?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnFestivalTabSelected -> {
                Log.d(TAG, "Festival tab selected: ${event.signGuCode.displayName}")
                setState { copy(selectedFestivalTab = event.signGuCode) }
                callFestivalList(event.signGuCode.code)
            }
            is LoungeEvent.OnFestivalItemClick -> {
                Log.d(TAG, "Festival item clicked - id: ${event.item.id}")
                event.item.id?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnFestivalMoreClick -> {
                Log.d(TAG, "Festival more clicked")
                setEffect { LoungeEffect.NavigateToFestivalList }
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

    /**
     * 내 주변 공연 검색 api
     */
    fun callMyAreaListApi(
        myAreaCode: String = state.value.selectedSignGuCode.code
    ) {
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

    /**
     * 장르별 랭킹 리스트 api
     */
    fun callGenreRankList(
        genreCode: String = state.value.selectedGenreTab.code
    ) {
        val startDate = DateUtil.getCurrentMonthFirstDay()
        val endDate = DateUtil.getCurrentMonthLastDay()

        viewModelScope.launch {
            performanceUseCase.getRankList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                genreCode = genreCode
            ).collect { rankList ->
                TimberUtil.d("test here getRankList = $rankList")
                rankList?.let { list ->
                    setState { copy(genreRankList = list) }
                }
            }
        }
    }

    fun callFestivalList(signGuCode: String) {
        val startDate = DateUtil.getCurrentMonthFirstDay()
        val endDate = DateUtil.getCurrentMonthLastDay()

        viewModelScope.launch {
            performanceUseCase.getFestivalList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                currentPage = "1",
                rowsPerPage = "10",
                signGuCode = signGuCode
            ).collect { festivalList ->
                TimberUtil.d("Festival list size: ${festivalList?.size ?: 0}")
                festivalList?.let { list ->
                    setState { copy(festivalList = list) }
                }
            }
        }
    }
}
