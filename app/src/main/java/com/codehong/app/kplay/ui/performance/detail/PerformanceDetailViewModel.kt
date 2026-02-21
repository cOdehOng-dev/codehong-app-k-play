package com.codehong.app.kplay.ui.performance.detail

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.model.favorite.FavoritePerformance
import com.codehong.app.kplay.domain.model.performance.detail.TicketingSite
import com.codehong.app.kplay.domain.type.ThemeType.Companion.toThemeType
import com.codehong.app.kplay.domain.usecase.FavoriteUseCase
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.app.kplay.domain.usecase.PlaceUseCase
import com.codehong.library.architecture.mvi.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerformanceDetailViewModel @Inject constructor(
    application: Application,
    private val performanceUseCase: PerformanceUseCase,
    private val placeUseCase: PlaceUseCase,
    private val favoriteUseCase: FavoriteUseCase
) : BaseViewModel<PerformanceDetailEvent, PerformanceDetailState, PerformanceDetailEffect>(
    application
) {

    override fun createInitialState(): PerformanceDetailState = PerformanceDetailState(
        themeType = performanceUseCase.getThemeType().toThemeType()
    )

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

            is PerformanceDetailEvent.OnBookingSiteClick -> {
                setEffect { PerformanceDetailEffect.OpenBookingSitePage(event.site) }
            }

            is PerformanceDetailEvent.OnHideBookingPicker -> {
                setState {
                    copy(
                        isShowReservationPicker = false
                    )
                }
            }

            is PerformanceDetailEvent.OnFavoriteClick -> {
                val detail = state.value.performanceDetail ?: return
                val id = detail.id ?: return
                viewModelScope.launch {
                    if (state.value.isFavorite) {
                        favoriteUseCase.removeFavorite(id)
                        setState { copy(isFavorite = false) }
                        setEffect { PerformanceDetailEffect.ShowFavoriteToast(false) }
                    } else {
                        favoriteUseCase.addFavorite(
                            FavoritePerformance(
                                id = id,
                                name = detail.name,
                                posterUrl = detail.posterUrl,
                                startDate = detail.startDate,
                                endDate = detail.endDate,
                                facilityName = detail.facilityName,
                                genre = detail.genre
                            )
                        )
                        setState { copy(isFavorite = true) }
                        setEffect { PerformanceDetailEffect.ShowFavoriteToast(true) }
                    }
                }
            }
        }
    }

    fun callReservationSiteListPicker(siteList: List<TicketingSite>?) {
        if (siteList.isNullOrEmpty()) return
        setState {
            copy(
                siteList = siteList,
                isShowReservationPicker = true
            )
        }
    }

    fun callPerformanceDetailApi(performanceId: String) {
        viewModelScope.launch {
            setState { copy(loading = loading.copy(isPerformanceDetailLoading = true)) }
            performanceUseCase.getPerformanceDetail(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                id = performanceId
            ).collect { result ->
                val performanceDetail = result?.firstOrNull()
                val isFavorite = if (!performanceDetail?.id.isNullOrEmpty()) {
                    favoriteUseCase.isFavorite(performanceDetail!!.id!!)
                } else {
                    false
                }
                setState {
                    copy(
                        performanceDetail = performanceDetail,
                        isFavorite = isFavorite,
                        loading = loading.copy(isPerformanceDetailLoading = false)
                    )
                }

                callPlaceSearchApi(performanceDetail?.facilityName)
            }
        }
    }

    fun callPlaceSearchApi(keyword: String?) {
        val keywordPlaceName = keyword?.split(" ")?.firstOrNull()
        if (keywordPlaceName.isNullOrEmpty()) {
            setState {
                copy(
                    placeDetail = null,
                    loading = loading.copy(isPlaceDetailLoading = false)
                )
            }
            return
        }
        viewModelScope.launch {
            setState { copy(loading = loading.copy(isPlaceDetailLoading = true)) }
            placeUseCase.searchPlace(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                keyword = keywordPlaceName,
                currentPage = "1",
                rowsPerPage = "50"
            ).collect { placeList ->
                val place =
                    placeList?.find { placeInfo -> keyword.contains(placeInfo.placeName ?: "") }
                val placeId = place?.placeId
                if (!placeId.isNullOrEmpty()) {
                    callPlaceDetailApi(placeId)
                } else {
                    setState {
                        copy(
                            placeDetail = null,
                            loading = loading.copy(isPlaceDetailLoading = false)
                        )
                    }
                }
            }
        }
    }

    fun callPlaceDetailApi(placeId: String) {
        viewModelScope.launch {
            placeUseCase.getPlaceDetail(
                serviceKey = BuildConfig.KOKOR_CLIENT_ID,
                id = placeId
            ).collect { detail ->
                setState {
                    copy(
                        placeDetail = detail,
                        loading = loading.copy(isPlaceDetailLoading = false)
                    )
                }
            }
        }
    }
}
