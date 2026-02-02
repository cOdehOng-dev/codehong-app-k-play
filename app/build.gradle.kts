plugins {
    alias(libs.plugins.codehong.android.application)
    alias(libs.plugins.codehong.android.application.compose)
    alias(libs.plugins.codehong.android.build.type)
    alias(libs.plugins.codehong.android.flavor)
    alias(libs.plugins.codehong.android.hilt)
    alias(libs.plugins.codehong.android.room)
    alias(libs.plugins.secrets.gradle.plugin)
}

android {
    namespace = project.properties["APP_ID"].toString()

    defaultConfig {
        applicationId = project.properties["APP_ID"].toString()
        versionName = project.properties["VERSION_NAME"].toString()

    }
}

secrets {
    propertiesFileName = "secrets.properties"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(codehonglibs.widget)
    implementation(codehonglibs.network)
    implementation(codehonglibs.architecture)
    implementation(codehonglibs.util)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.material)
    implementation(libs.composables.core)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.google.android.gms.location)

    implementation(libs.naver.map.compose)
    implementation(libs.naver.map.location)
    implementation(libs.naver.map.sdk)

    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.palette)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.datasource)
    implementation(libs.google.android.gms.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
