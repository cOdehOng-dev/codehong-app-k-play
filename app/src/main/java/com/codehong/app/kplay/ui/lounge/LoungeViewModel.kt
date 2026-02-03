package com.codehong.app.kplay.ui.lounge

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.library.architecture.mvi.BaseViewModel
import com.codehong.library.network.debug.TimberUtil
import com.codehong.library.util.DateUtil
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
                TimberUtil.d("Tab selected: ${event.tab.title}")
                setState { copy(selectedTab = event.tab) }
            }
            is LoungeEvent.OnCategoryClick -> {
                TimberUtil.d("Category clicked: ${event.genreCode.displayName} (code: ${event.genreCode.code})")
                setEffect { LoungeEffect.NavigateToCategory(event.genreCode) }
            }
            is LoungeEvent.OnRankTabSelected -> {
                TimberUtil.d("Rank tab selected: ${event.rankTab.title}")
                setState { copy(selectedRankTab = event.rankTab) }
            }
            is LoungeEvent.OnRankItemClick -> {
                TimberUtil.d("Rank item clicked - performanceId: ${event.item.performanceId}")
                event.item.performanceId?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnRefreshNearbyClick -> {
                TimberUtil.d("Refresh nearby clicked")
                setEffect { LoungeEffect.RequestLocationPermission }
            }
            is LoungeEvent.OnNearbyItemClick -> {
                TimberUtil.d("Nearby item clicked - id: ${event.item.id}")
                event.item.id?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnSignGuCodeUpdated -> {
                TimberUtil.d("SignGuCode updated: ${event.signGuCode.displayName}")
                setState { copy(selectedSignGuCode = event.signGuCode) }
                callMyAreaListApi(event.signGuCode.code)
            }
            is LoungeEvent.OnGenreTabSelected -> {
                TimberUtil.d("Genre tab selected: ${event.genreCode.displayName}")
                setState { copy(selectedGenreTab = event.genreCode, isGenreRankLoaded = false, genreRankList = emptyList()) }
                callGenreRankList(event.genreCode.code)
            }
            is LoungeEvent.OnGenreRankItemClick -> {
                TimberUtil.d("Genre rank item clicked - performanceId: ${event.item.performanceId}")
                event.item.performanceId?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnGenreRankMoreClick -> {
                TimberUtil.d("Genre rank more clicked - selectedGenreTab: ${state.value.selectedGenreTab.code}")
                setEffect { LoungeEffect.NavigateToGenreRankList(state.value.selectedGenreTab) }
            }
            is LoungeEvent.OnFestivalTabSelected -> {
                TimberUtil.d("Festival tab selected: ${event.signGuCode.displayName}")
                setState { copy(selectedFestivalTab = event.signGuCode, isFestivalLoaded = false, festivalList = emptyList()) }
                callFestivalList(event.signGuCode.code)
            }
            is LoungeEvent.OnFestivalItemClick -> {
                TimberUtil.d("Festival item clicked - id: ${event.item.id}")
                event.item.id?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnFestivalMoreClick -> {
                TimberUtil.d("Festival more clicked")
                setEffect { LoungeEffect.NavigateToFestivalList }
            }
            is LoungeEvent.OnAwardedTabSelected -> {
                TimberUtil.d("Awarded tab selected: ${event.signGuCode.displayName}")
                setState { copy(selectedAwardedTab = event.signGuCode, isAwardedLoaded = false, awardedList = emptyList()) }
                callAwardedPerformanceList(event.signGuCode.code)
            }
            is LoungeEvent.OnAwardedItemClick -> {
                TimberUtil.d("Awarded item clicked - id: ${event.item.id}")
                event.item.id?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnAwardedMoreClick -> {
                TimberUtil.d("Awarded more clicked")
                // TODO: 수상작 리스트 페이지 이동
                setEffect { LoungeEffect.NavigateToAwardedList }
            }
            is LoungeEvent.OnLocalTabSelected -> {
                TimberUtil.d("Local tab selected: ${event.signGuCode.displayName}")
                setState { copy(selectedLocalTab = event.signGuCode, isLocalLoaded = false, localList = emptyList()) }
                callLocalList(event.signGuCode.code)
            }
            is LoungeEvent.OnLocalItemClick -> {
                TimberUtil.d("Local item clicked - id: ${event.item.id}")
                event.item.id?.let {
                    setEffect { LoungeEffect.NavigateToPerformanceDetail(it) }
                }
            }
            is LoungeEvent.OnLocalMoreClick -> {
                TimberUtil.d("Local more clicked - selectedLocalTab: ${state.value.selectedLocalTab.code}")
                setEffect { LoungeEffect.NavigateToLocalList(state.value.selectedLocalTab) }
            }
        }
    }

    fun callRankList() {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        setState {
            copy(
                loading = loading.copy(isBannerLoading = true)
            )
        }
        viewModelScope.launch {
            performanceUseCase.getRankList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate
            ).collect { rankList ->
                TimberUtil.d("Rank list size: ${rankList?.size ?: 0}")
                if (rankList.isNullOrEmpty()) return@collect

                setState {
                    copy(
                        rankList = rankList,
                        currentMonth = DateUtil.getCurrentMonth(),
                        loading = loading.copy(isBannerLoading = false)
                    )
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
        val startDate = DateUtil.getToday(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getOneMonthLater(Consts.YYYY_MM_DD_FORMAT)

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
                setState { copy(myAreaList = list ?: emptyList(), isMyAreaLoaded = true) }
            }
        }
    }

    /**
     * 지역별 공연 리스트 api
     */
    fun callLocalList(
        areaCode: String = state.value.selectedLocalTab.code
    ) {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        viewModelScope.launch {
            performanceUseCase.getPerformanceList(
                service = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                currentPage = "1",
                rowsPerPage = "10",
                signGuCode = areaCode,
            ).collect { list ->
                TimberUtil.d("Local list size: ${list?.size ?: 0}")
                setState { copy(localList = list ?: emptyList(), isLocalLoaded = true) }
            }
        }
    }

    /**
     * 장르별 랭킹 리스트 api
     */
    fun callGenreRankList(
        genreCode: String = state.value.selectedGenreTab.code
    ) {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        viewModelScope.launch {
            performanceUseCase.getRankList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                genreCode = genreCode
            ).collect { rankList ->
                TimberUtil.d("test here getRankList = $rankList")
                setState { copy(genreRankList = rankList ?: emptyList(), isGenreRankLoaded = true) }
            }
        }
    }

    fun callFestivalList(signGuCode: String) {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

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
                setState { copy(festivalList = festivalList ?: emptyList(), isFestivalLoaded = true) }
            }
        }
    }

    fun callAwardedPerformanceList(signGuCode: String) {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        viewModelScope.launch {
            performanceUseCase.getAwardedPerformanceList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                currentPage = "1",
                rowsPerPage = "10",
                signGuCode = signGuCode
            ).collect { awardedList ->
                TimberUtil.d("Awarded performance list size: ${awardedList?.size ?: 0}")
                setState { copy(awardedList = awardedList ?: emptyList(), isAwardedLoaded = true) }
            }
        }
    }
}
