plugins {
    alias(libs.plugins.codehong.android.library)
    alias(libs.plugins.codehong.android.hilt)
    alias(libs.plugins.codehong.android.network)
}

android {
    namespace = "com.codehong.app.kplay.domain"
}
dependencies {
    implementation(codehonglibs.network)
    implementation(codehonglibs.architecture)
    implementation(codehonglibs.util)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
