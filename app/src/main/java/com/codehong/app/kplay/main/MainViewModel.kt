package com.codehong.app.kplay.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codehong.app.kplay.domain.usecase.PerformanceUseCase
import com.codehong.library.network.debug.TimberUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val performanceUseCase: PerformanceUseCase
) : ViewModel() {

    fun callPerformanceListApi() {
        viewModelScope.launch {
            TimberUtil.d("test here call api")
            performanceUseCase.getPerformanceList(
                startDate = "20260101",
                endDate = "20260630",
                currentPage = "1",
                rowsPerPage = "30"
            ).collect {
                TimberUtil.d("test here 111 = $it")
            }
        }
    }
}