package com.codehong.app.kplay.ui.lounge

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.codehong.app.kplay.manager.ActivityManager
import com.codehong.app.kplay.ui.theme.CodehongappkplayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoungeActivity : ComponentActivity() {

    private val viewModel by viewModels<LoungeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.callRankList()

        setContent {
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is LoungeEffect.NavigateToCategory -> {
                            ActivityManager.openGenreList(
                                this@LoungeActivity,
                                effect.genreCode.code
                            )
                        }
                        is LoungeEffect.NavigateToPerformanceDetail -> {
                            ActivityManager.openPerformanceDetail(
                                this@LoungeActivity,
                                effect.performanceId
                            )
                        }
                        is LoungeEffect.ShowToast -> {
                            Toast.makeText(
                                this@LoungeActivity,
                                effect.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            CodehongappkplayTheme {
                LoungeScreen(viewModel = viewModel)
            }
        }
    }
}
