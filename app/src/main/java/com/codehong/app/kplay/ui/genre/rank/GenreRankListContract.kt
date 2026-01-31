package com.codehong.app.kplay.ui.genre.rank

import com.codehong.app.kplay.base.ViewEvent
import com.codehong.app.kplay.base.ViewSideEffect
import com.codehong.app.kplay.base.ViewState
import com.codehong.app.kplay.domain.model.BoxOfficeItem
import com.codehong.app.kplay.domain.type.GenreCode

data class GenreRankListState(
    val initialGenreCode: GenreCode = GenreCode.THEATER,
    val selectedGenreCode: GenreCode = GenreCode.THEATER,
    val startDate: String = "",  // yyyyMMdd
    val endDate: String = "",    // yyyyMMdd
    val currentPage: Int = 1,
    val genreRankList: List<BoxOfficeItem> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true
) : ViewState

sealed class GenreRankListEvent : ViewEvent {
    data class OnRankItemClick(val item: BoxOfficeItem) : GenreRankListEvent()
    data class OnGenreCodeSelected(val genreCode: GenreCode) : GenreRankListEvent()
    data class OnDateSelected(val startDate: String, val endDate: String) : GenreRankListEvent()
    data object OnLoadMore : GenreRankListEvent()
    data object OnDateChangeClick : GenreRankListEvent()
}

sealed class GenreRankListEffect : ViewSideEffect {
    data class NavigateToDetail(val performanceId: String) : GenreRankListEffect()
    data object ShowDatePicker : GenreRankListEffect()
}
