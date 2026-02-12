package com.codehong.app.kplay.ui.performance.detail

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.BuildConfig
import com.codehong.app.kplay.domain.model.performance.detail.TicketingSite
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
    private val placeUseCase: PlaceUseCase
) : BaseViewModel<PerformanceDetailEvent, PerformanceDetailState, PerformanceDetailEffect>(
    application
) {

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
                setState {
                    copy(
                        performanceDetail = performanceDetail,
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
