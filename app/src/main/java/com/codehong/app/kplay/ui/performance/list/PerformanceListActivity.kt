package com.codehong.app.kplay.ui.performance.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PerformanceListActivity : ComponentActivity() {

    private val viewModel by viewModels<PerformanceListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}