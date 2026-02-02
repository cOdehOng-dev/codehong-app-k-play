plugins {
    alias(libs.plugins.codehong.android.library)
    alias(libs.plugins.codehong.android.hilt)
}

android {
    namespace = "com.codehong.app.kplay.domain"
}
dependencies {
    implementation(codehonglibs.network)
    implementation(codehonglibs.architecture)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
