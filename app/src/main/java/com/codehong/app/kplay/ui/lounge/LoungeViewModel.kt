package com.codehong.app.kplay.ui.lounge

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.Consts
import com.codehong.app.kplay.domain.model.PerformanceInfoItem
import com.codehong.app.kplay.domain.type.BottomTabType
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.domain.type.ThemeType.Companion.toThemeType
import com.codehong.app.kplay.domain.usecase.FavoriteUseCase
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.app.kplay.domain.usecase.PlaceUseCase
import com.codehong.app.kplay.ui.lounge.content.mylocation.VenueGroup
import com.codehong.library.architecture.mvi.BaseViewModel
import com.codehong.library.debugtool.log.TimberUtil
import com.codehong.library.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoungeViewModel"

@HiltViewModel
class LoungeViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase,
    private val favoriteUseCase: FavoriteUseCase,
    private val placeUseCase: PlaceUseCase
) : BaseViewModel<LoungeEvent, LoungeState, LoungeEffect>(application) {

    // 마지막으로 위경도 조회를 완료한 도시 코드 (캐시 키)
    private var lastResolvedSignGuCode: SignGuCode? = null

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
                if (event.tab == BottomTabType.MY_LOCATION) {
                    val currentSignGuCode = state.value.selectedSignGuCode
                    val alreadyResolved = lastResolvedSignGuCode == currentSignGuCode
                    val hasVenues = state.value.venueGroups.isNotEmpty()
                    if (!alreadyResolved || !hasVenues) {
                        resolveVenueCoordinates(state.value.myAreaList)
                    } else {
                        TimberUtil.d("$TAG ▶ venue 이미 조회됨 (${currentSignGuCode.displayName}), skip")
                    }
                }
                if (event.tab == BottomTabType.SETTINGS) {
                    loadCacheSize()
                }
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
                if (state.value.selectedSignGuCode != event.signGuCode) {
                    // 도시가 변경되면 venue 캐시 무효화
                    lastResolvedSignGuCode = null
                    setState {
                        copy(
                            selectedSignGuCode = event.signGuCode,
                            venueGroups = emptyList()
                        )
                    }
                } else {
                    setState { copy(selectedSignGuCode = event.signGuCode) }
                }
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

            is LoungeEvent.OnCacheDeleteConfirmed -> {
                viewModelScope.launch {
                    placeUseCase.clearCache()
                    loadCacheSize()
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
                copy(apiLoading = apiLoading.copy(isMyAreaLoading = true))
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
     * 내 주변 탭 선택 시 호출.
     * placeName 기준으로 공연장을 그룹화하고 PlaceUseCase로 위경도를 조회하여 VenueGroup 생성.
     * 같은 도시에서 이미 핀을 찍은 적이 있으면 호출되지 않음.
     */
    private fun resolveVenueCoordinates(performances: List<PerformanceInfoItem>) {
        val targetSignGuCode = state.value.selectedSignGuCode
        viewModelScope.launch {
            setState {
                copy(apiLoading = apiLoading.copy(isVenueGroupLoading = true))
            }

            val grouped = performances.groupBy { it.placeName ?: "알 수 없음" }
            val venueGroups = mutableListOf<VenueGroup>()

            grouped.forEach { (placeName, items) ->
                val detail = placeUseCase.getPlaceDetail(
                    serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                    keyword = placeName,
                    currentPage = "1",
                    rowsPerPage = "5"
                ).firstOrNull()

                val lat = detail?.latitude?.toDoubleOrNull()
                val lng = detail?.longitude?.toDoubleOrNull()

                TimberUtil.d("venue=$placeName lat=$lat lng=$lng performances=${items.size}")

                venueGroups.add(
                    VenueGroup(
                        placeName = placeName,
                        lat = lat,
                        lng = lng,
                        performances = items
                    )
                )
            }

            lastResolvedSignGuCode = targetSignGuCode
            setState {
                copy(
                    venueGroups = venueGroups,
                    apiLoading = apiLoading.copy(isVenueGroupLoading = false)
                )
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

    private fun loadCacheSize() {
        viewModelScope.launch {
            val bytes = placeUseCase.getCacheSize()
            val text = formatCacheSize(bytes)
            setState { copy(cacheSizeText = text) }
        }
    }

    private fun formatCacheSize(bytes: Long): String {
        return when {
            bytes <= 0L -> "0 MB"
            bytes < 1024L -> "${bytes} B"
            bytes < 1024L * 1024L -> String.format("%.2f KB", bytes / 1024.0)
            else -> String.format("%.2f MB", bytes / (1024.0 * 1024.0))
        }
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
