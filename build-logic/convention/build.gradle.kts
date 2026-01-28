plugins {
    `kotlin-dsl`
}

group = "com.codehong.core.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.android.tools.common)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "codehong.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "codehong.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "codehong.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "codehong.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("publishingLibrary") {
            id = "codehong.library.publishing"
            implementationClass = "PublishingLibraryConventionPlugin"
        }
    }
}