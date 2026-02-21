package com.codehong.app.kplay.ui.lounge

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.domain.type.ThemeType.Companion.toThemeType
import com.codehong.app.kplay.domain.usecase.FavoriteUseCase
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.library.architecture.mvi.BaseViewModel
import com.codehong.library.debugtool.log.TimberUtil
import com.codehong.library.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoungeViewModel"

@HiltViewModel
class LoungeViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : BaseViewModel<LoungeEvent, LoungeState, LoungeEffect>(application) {

    init {
        observeFavorites()
    }

    override fun createInitialState(): LoungeState {
        return LoungeState(
            currentMonth = DateUtil.getCurrentMonth(),
            themeType = performanceUseCase.getThemeType().toThemeType()
        )
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteUseCase.getFavoriteList().collect { favoriteList ->
                setState { copy(favoriteList = favoriteList) }
            }
        }
    }

    override fun handleEvents(event: LoungeEvent) {
        when (event) {
            is LoungeEvent.OnTabSelected -> {
                TimberUtil.d("Tab selected: ${event.tab.label}")
                setState { copy(selectedTab = event.tab) }
            }

            is LoungeEvent.OnCategoryClick -> {
                TimberUtil.d("Category clicked: ${event.genreCode.displayName} (code: ${event.genreCode.code})")
                setEffect { LoungeEffect.NavigateToCategory(event.genreCode) }
            }

            is LoungeEvent.OnRankTabSelected -> {
                TimberUtil.d("Rank tab selected: ${event.rankTab.display}")
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
                setMyLocation(event.signGuCode.code)
                callMyAreaListApi(event.signGuCode.code)
            }

            is LoungeEvent.OnGenreTabSelected -> {
                TimberUtil.d("Genre tab selected: ${event.genreCode.displayName}")
                setState {
                    copy(
                        selectedGenreTab = event.genreCode,
                        genreRankList = emptyList(),
                        apiLoading = apiLoading.copy(isGenreRankingLoading = false),
                    )
                }
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
                setState {
                    copy(
                        selectedFestivalTab = event.signGuCode,
                        apiLoading = apiLoading.copy(isFestivalLoading = false),
                        festivalList = emptyList()
                    )
                }
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
                setState {
                    copy(
                        selectedAwardedTab = event.signGuCode,
                        isAwardedLoaded = false,
                        awardedList = emptyList()
                    )
                }
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
                setEffect { LoungeEffect.NavigateToAwardedList }
            }

            is LoungeEvent.OnLocalTabSelected -> {
                TimberUtil.d("Local tab selected: ${event.signGuCode.displayName}")
                setState {
                    copy(
                        selectedLocalTab = event.signGuCode,
                        apiLoading = apiLoading.copy(isLocalLoading = false),
                        localList = emptyList()
                    )
                }
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

            is LoungeEvent.OnThemeChanged -> {
                setState { copy(themeType = event.themeType) }
                performanceUseCase.setThemeType(event.themeType.name)
            }

            is LoungeEvent.OnFavoriteItemClick -> {
                setEffect { LoungeEffect.NavigateToPerformanceDetail(event.id) }
            }

            is LoungeEvent.OnFavoriteItemDelete -> {
                viewModelScope.launch {
                    favoriteUseCase.removeFavorite(event.id)
                }
            }
        }
    }

    fun callRankList() {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        setState {
            copy(
                apiLoading = apiLoading.copy(isMonthRankLoading = true)
            )
        }

        viewModelScope.launch {
            performanceUseCase.getRankList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate
            ).collect { rankList ->

                setState {
                    copy(
                        rankList = rankList ?: emptyList(),
                        currentMonth = DateUtil.getCurrentMonth(),
                        apiLoading = apiLoading.copy(isMonthRankLoading = false)
                    )
                }
            }
        }
    }

    /**
     * 내 주변 공연 검색 api
     */
    fun callMyAreaListApi(myAreaCode: String) {
        val startDate = DateUtil.getToday(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getOneMonthLater(Consts.YYYY_MM_DD_FORMAT)

        viewModelScope.launch {
            setState {
                copy(
                    apiLoading = apiLoading.copy(isMyAreaLoading = true)
                )
            }
            performanceUseCase.getPerformanceList(
                service = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                currentPage = "1",
                rowsPerPage = "100",
                signGuCode = myAreaCode,
            ).collect { myAreaList ->
                setState {
                    copy(
                        myAreaList = myAreaList ?: emptyList(),
                        apiLoading = apiLoading.copy(isMyAreaLoading = false)
                    )
                }
            }
        }
    }

    /**
     * 지역별 공연 리스트 api
     */
    fun callLocalList(areaCode: String) {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        viewModelScope.launch {
            setState {
                copy(
                    apiLoading = apiLoading.copy(isLocalLoading = true)
                )
            }
            performanceUseCase.getPerformanceList(
                service = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                currentPage = "1",
                rowsPerPage = "10",
                signGuCode = areaCode,
            ).collect { localList ->
                setState {
                    copy(
                        localList = localList ?: emptyList(),
                        apiLoading = apiLoading.copy(isLocalLoading = false)
                    )
                }
            }
        }
    }

    /**
     * 장르별 랭킹 리스트 api
     */
    fun callGenreRankList(genreCode: String) {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        viewModelScope.launch {
            setState {
                copy(
                    apiLoading = apiLoading.copy(isGenreRankingLoading = true)
                )
            }
            performanceUseCase.getRankList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                genreCode = genreCode
            ).collect { rankList ->
                setState {
                    copy(
                        genreRankList = rankList ?: emptyList(),
                        apiLoading = apiLoading.copy(isGenreRankingLoading = false)
                    )
                }
            }
        }
    }

    fun callFestivalList(signGuCode: String) {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        viewModelScope.launch {
            setState {
                copy(
                    apiLoading = apiLoading.copy(isFestivalLoading = true)
                )
            }
            performanceUseCase.getFestivalList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                currentPage = "1",
                rowsPerPage = "10",
                signGuCode = signGuCode
            ).collect { festivalList ->
                setState {
                    copy(
                        festivalList = festivalList ?: emptyList(),
                        apiLoading = apiLoading.copy(isFestivalLoading = false)
                    )
                }
            }
        }
    }

    fun callAwardedPerformanceList(signGuCode: String) {
        val startDate = DateUtil.getPreviousMonthFirstDay(Consts.YYYY_MM_DD_FORMAT)
        val endDate = DateUtil.getPreviousMonthLastDay(Consts.YYYY_MM_DD_FORMAT)

        viewModelScope.launch {
            setState {
                copy(
                    apiLoading = apiLoading.copy(isAwardLoading = true)
                )
            }
            performanceUseCase.getAwardedPerformanceList(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                startDate = startDate,
                endDate = endDate,
                currentPage = "1",
                rowsPerPage = "10",
                signGuCode = signGuCode
            ).collect { awardedList ->
                setState {
                    copy(
                        awardedList = awardedList ?: emptyList(),
                        apiLoading = apiLoading.copy(isAwardLoading = false)
                    )
                }
            }
        }
    }

    fun setMyLocation(signGuCode: String) {
        performanceUseCase.setMyLocation(signGuCode)
    }

    fun callMyLocation() {
        val storedSignGuCodeName = performanceUseCase.getMyLocation()
        val initialSignGuCode = if (storedSignGuCodeName != null) {
            try {
                SignGuCode.valueOf(storedSignGuCodeName)
            } catch (e: IllegalArgumentException) {
                SignGuCode.SEOUL
            }
        } else {
            SignGuCode.SEOUL
        }
        setEvent(LoungeEvent.OnSignGuCodeUpdated(initialSignGuCode))
    }
}
