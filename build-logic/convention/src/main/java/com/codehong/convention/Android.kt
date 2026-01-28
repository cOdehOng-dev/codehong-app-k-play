package com.codehong.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun Project.configureAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    isCompose: Boolean,
) {
    commonExtension.apply {
        compileSdk = libs.getVersion("compileSdk")

        defaultConfig {
            minSdk = libs.getVersion("minSdk")
            vectorDrawables.useSupportLibrary = true
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        buildFeatures {
            buildConfig = true
//            viewBinding = true
//            dataBinding {
//                isEnabled = true
//            }
            compose = isCompose
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        lint {
            abortOnError = false
        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinExtension.jvmToolchain(17)

    dependencies {
        if (isCompose) {
            val bom = libs.getLibrary("androidx-compose-bom")
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
        }
    }


}

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            isMinifyEnabled = false
                            isCrunchPngs = false
//                            proguardFiles(
//                                commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
//                                "proguard-rules.pro"
//                            )
                            proguardFile("proguard-rules.pro")

                            tasks.whenTaskAdded {
                                if (name == "lint") {
                                    enabled = false
                                }
                            }
                        }
                        release {
                            isMinifyEnabled = true
                            isCrunchPngs = false
//                            proguardFiles(
//                                commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
//                                "proguard-rules.pro"
//                            )
                            proguardFile("proguard-rules.pro")
                        }
                    }
                }
            }
            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    val versionName = project.getStringProperty("VERSION_NAME", "")

                    buildTypes {
                        debug {
                            isMinifyEnabled = false
//                            proguardFiles(
//                                commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
//                                "proguard-rules.pro"
//                            )
                            buildConfigField(
                                "String",
                                "VERSION_NAME",
                                "\"$versionName\""
                            )
                            tasks.whenTaskAdded {
                                if (name == "lint") {
                                    enabled = false
                                }
                            }
                        }
                        release {
                            isMinifyEnabled = false
                            buildConfigField(
                                "String",
                                "VERSION_NAME",
                                "\"$versionName\""
                            )
//                            proguardFiles(
//                                commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
//                                "proguard-rules.pro"
//                            )
                        }
                    }
                }
            }
        }
    }
}

internal fun Project.configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.apply {
        flavorDimensions += "mode"
        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    val appId = project.getStringProperty("APP_ID", "")
                    val appName = project.getStringProperty("APP_NAME", "")
                    val injectVersionCode = project.getIntProperty("VERSION_CODE", 0)
                    val date = SimpleDateFormat("MMddHHmm", Locale.KOREA).format(Date()).toInt()
                    productFlavors {
                        create("dev") {
                            dimension = "mode"
                            sourceSets.getByName("dev").res.srcDirs("src/dev/res")

                            applicationIdSuffix = ".dev"
                            manifestPlaceholders["appId"] = "${appId}.dev"
                            manifestPlaceholders["appName"] = "${appName}-dev"

                            versionCode = injectVersionCode + date
                        }
                        create("prod") {
                            dimension = "mode"

                            applicationIdSuffix = ""
                            manifestPlaceholders["appId"] = appId
                            manifestPlaceholders["appName"] = appName

                            versionCode = injectVersionCode
                        }
                    }
                }
            }
            ExtensionType.LIBRARY -> {
//                extensions.configure<LibraryExtension> {
//                    productFlavors {
//                        create("dev") {}
//                        create("prod") {}
//                    }
//                }
            }
        }
    }
}
