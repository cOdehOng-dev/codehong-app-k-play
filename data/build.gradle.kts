plugins {
    alias(libs.plugins.codehong.android.library)
    alias(libs.plugins.codehong.android.hilt)
}

android {
    namespace = "com.codehong.app.kplay.data"
}

dependencies {
    implementation(project(":domain"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}