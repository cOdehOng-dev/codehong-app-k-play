plugins {
    alias(libs.plugins.codehong.android.library)
    alias(libs.plugins.codehong.android.hilt)
}

android {
    namespace = "com.codehong.app.kplay.domain"
}
dependencies {
    // coroutines
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
