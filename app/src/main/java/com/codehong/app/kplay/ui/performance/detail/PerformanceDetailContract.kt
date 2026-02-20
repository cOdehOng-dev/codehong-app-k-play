package com.codehong.app.kplay.ui.performance.detail

import com.codehong.app.kplay.domain.model.performance.detail.PerformanceDetail
import com.codehong.app.kplay.domain.model.performance.detail.TicketingSite
import com.codehong.app.kplay.domain.model.place.PlaceDetail
import com.codehong.app.kplay.domain.type.ThemeType
import com.codehong.app.kplay.domain.util.toPeriod
import com.codehong.library.architecture.mvi.ViewEvent
import com.codehong.library.architecture.mvi.ViewSideEffect
import com.codehong.library.architecture.mvi.ViewState

data class PerformanceDetailState(
    val performanceDetail: PerformanceDetail? = null,
    val placeDetail: PlaceDetail? = null,
    val siteList: List<TicketingSite> = emptyList(),
    val isShowReservationPicker: Boolean = false,
    val loading: PerformanceDetailLoading = PerformanceDetailLoading(),
    val themeType: ThemeType = ThemeType.SYSTEM
) : ViewState {

    val period: String
        get() = Pair(
            performanceDetail?.startDate,
            performanceDetail?.endDate
        ).toPeriod()
    val realSiteList : List<TicketingSite>
        get() = siteList.filter { !it.url.isNullOrBlank() }

    val siteNameList: List<String>
        get() {
            val nameList = mutableListOf<String>()
            realSiteList.forEach {
                if (!it.name.isNullOrBlank()) {
                    nameList.add(it.name!!)
                }
            }
            return nameList
        }
}

sealed class PerformanceDetailEvent : ViewEvent {
    data object OnBookingClick : PerformanceDetailEvent()
    data object OnBackClick : PerformanceDetailEvent()

    data class OnBookingSiteClick(val site: String?) : PerformanceDetailEvent()

    data object OnHideBookingPicker : PerformanceDetailEvent()
}

sealed class PerformanceDetailEffect : ViewSideEffect {
    data object NavigateBack : PerformanceDetailEffect()
    data class OpenBookingPage(val relates: List<TicketingSite>) : PerformanceDetailEffect()

    data class OpenBookingSitePage(val site: String?) : PerformanceDetailEffect()

}

data class PerformanceDetailLoading(
    val isPerformanceDetailLoading: Boolean = true,
    val isPlaceDetailLoading: Boolean = true
) {
    val isLoading: Boolean
        get() = isPerformanceDetailLoading || isPlaceDetailLoading
}
