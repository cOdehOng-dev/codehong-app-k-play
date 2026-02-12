plugins {
    alias(libs.plugins.codehong.android.library)
    alias(libs.plugins.codehong.android.hilt)
    alias(libs.plugins.codehong.android.network)
}

android {
    namespace = "com.codehong.app.kplay.data"
}

dependencies {
    implementation(project(":domain"))

    implementation(codehonglibs.network)
    implementation(codehonglibs.architecture)
    implementation(codehonglibs.util)
    implementation(codehonglibs.debugtool)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}