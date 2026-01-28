package com.codehong.app.kplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.codehong.app.kplay.map.MapActivity
import com.codehong.app.kplay.ui.theme.CodehongappkplayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodehongappkplayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            val intent = Intent(context, MapActivity::class.java).apply {
                putExtra("latitude", 37.5666102)
                putExtra("longitude", 126.9783881)
                putExtra("title", "서울시청")
                putExtra("description", "서울특별시 중구 세종대로 110")
            }
            context.startActivity(intent)
        }) {
            Text("지도 보기")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CodehongappkplayTheme {
        MainScreen()
    }
}