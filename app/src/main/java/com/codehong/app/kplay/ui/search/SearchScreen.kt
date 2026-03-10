package com.codehong.app.kplay.ui.search

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import com.callstack.reactnativebrownfield.ReactNativeFragment

@Composable
fun SearchScreen(
    activity: AppCompatActivity
) {
    val containerId = remember { View.generateViewId() }

    AndroidView(
        factory = { ctx ->
            FragmentContainerView(ctx).apply {
                id = containerId

                val fm = activity.supportFragmentManager
                if (fm.findFragmentById(containerId) == null) {
                    val rnFragment = ReactNativeFragment
                        .createReactNativeFragment("SettingScreen")
                    fm.beginTransaction()
                        .replace(containerId, rnFragment)
                        .commitAllowingStateLoss()
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}