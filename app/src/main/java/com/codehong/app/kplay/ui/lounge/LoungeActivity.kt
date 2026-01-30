package com.codehong.app.kplay.ui.lounge

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import com.codehong.app.kplay.domain.type.SignGuCode
import com.codehong.app.kplay.manager.ActivityManager
import com.codehong.app.kplay.ui.theme.CodehongappkplayTheme
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

private const val TAG = "LoungeActivity"

@AndroidEntryPoint
class LoungeActivity : ComponentActivity() {

    private val viewModel by viewModels<LoungeViewModel>()
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.callRankList()
        viewModel.callMyAreaListApi()

        setContent {
            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

                if (fineLocationGranted || coarseLocationGranted) {
                    Log.d(TAG, "Location permission granted")
                    getLocationAndUpdateSignGuCode()
                } else {
                    Log.d(TAG, "Location permission denied, using default SEOUL")
                    Toast.makeText(
                        this@LoungeActivity,
                        "위치 권한이 거부되어 서울 기준으로 조회합니다",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.setEvent(LoungeEvent.OnSignGuCodeUpdated(SignGuCode.SEOUL))
                }
            }

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
                        is LoungeEffect.RequestLocationPermission -> {
                            if (hasLocationPermission()) {
                                getLocationAndUpdateSignGuCode()
                            } else {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }
                        }
                    }
                }
            }

            CodehongappkplayTheme {
                LoungeScreen(viewModel = viewModel)
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocationAndUpdateSignGuCode() {
        if (!hasLocationPermission()) {
            viewModel.setEvent(LoungeEvent.OnSignGuCodeUpdated(SignGuCode.SEOUL))
            return
        }

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(TAG, "Location: ${location.latitude}, ${location.longitude}")
                    val geocoder = Geocoder(this, Locale.KOREA)
                    try {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val adminArea = address.adminArea
                            Log.d(TAG, "Admin area: $adminArea")

                            val signGuCode = findSignGuCodeByName(adminArea)
                            viewModel.setEvent(LoungeEvent.OnSignGuCodeUpdated(signGuCode))
                        } else {
                            viewModel.setEvent(LoungeEvent.OnSignGuCodeUpdated(SignGuCode.SEOUL))
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Geocoder error: ${e.message}")
                        viewModel.setEvent(LoungeEvent.OnSignGuCodeUpdated(SignGuCode.SEOUL))
                    }
                } else {
                    Log.d(TAG, "Location is null, using default SEOUL")
                    viewModel.setEvent(LoungeEvent.OnSignGuCodeUpdated(SignGuCode.SEOUL))
                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to get location: ${e.message}")
                viewModel.setEvent(LoungeEvent.OnSignGuCodeUpdated(SignGuCode.SEOUL))
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception: ${e.message}")
            viewModel.setEvent(LoungeEvent.OnSignGuCodeUpdated(SignGuCode.SEOUL))
        }
    }

    private fun findSignGuCodeByName(areaName: String?): SignGuCode {
        if (areaName.isNullOrBlank()) return SignGuCode.SEOUL

        return SignGuCode.entries.find { signGuCode ->
            areaName.contains(signGuCode.displayName.take(2))
        } ?: SignGuCode.SEOUL
    }
}
