package com.codehong.app.kplay.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.codehong.library.widget.util.HongToastUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.naver.maps.geometry.LatLng

class MapActivity : ComponentActivity() {

    companion object {
        // 서울 시청 좌표
        val SEOUL_CITY_HALL = LatLng(37.5666805, 126.9784147)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myLocation by mutableStateOf<LatLng?>(null)
    private var initialCameraPosition by mutableStateOf<LatLng?>(null)
    private var isInitialLocationSet by mutableStateOf(false)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                getCurrentLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                getCurrentLocation()
            }
            else -> {
                HongToastUtil.showToast(this, "위치 권한이 필요합니다.")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val title = intent.getStringExtra("title") ?: ""
        val description = intent.getStringExtra("description") ?: ""

        // 진입 시 위치 권한 체크 후 초기 카메라 위치 설정
        initializeLocation()

        setContent {
            MapScreen(
                title = title,
                description = description,
                initialCameraPosition = initialCameraPosition,
                myLocation = myLocation,
                onMyLocationClick = { checkLocationPermissionAndGetLocation() }
            )
        }
    }

    private fun initializeLocation() {
        if (hasLocationPermission()) {
            getInitialLocation()
        } else {
            // 권한 없으면 서울 시청으로 설정
            initialCameraPosition = SEOUL_CITY_HALL
            isInitialLocationSet = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun getInitialLocation() {
        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            location?.let {
                val currentLocation = LatLng(it.latitude, it.longitude)
                myLocation = currentLocation
                initialCameraPosition = currentLocation
            } ?: run {
                // 위치를 가져올 수 없으면 서울 시청으로 설정
                initialCameraPosition = SEOUL_CITY_HALL
            }
            isInitialLocationSet = true
        }.addOnFailureListener {
            // 실패 시 서울 시청으로 설정
            initialCameraPosition = SEOUL_CITY_HALL
            isInitialLocationSet = true
        }
    }

    private fun checkLocationPermissionAndGetLocation() {
        when {
            hasLocationPermission() -> {
                getCurrentLocation()
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (!hasLocationPermission()) return

        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            location?.let {
                myLocation = LatLng(it.latitude, it.longitude)
                Toast.makeText(
                    this,
                    "내 위치: ${it.latitude}, ${it.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            } ?: run {
                Toast.makeText(this, "위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "위치 가져오기 실패: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}