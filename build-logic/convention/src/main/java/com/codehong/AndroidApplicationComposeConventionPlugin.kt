
import com.android.build.api.dsl.ApplicationExtension
import com.codehong.convention.ExtensionType
import com.codehong.convention.configureAndroid
import com.codehong.convention.configureBuildTypes
import com.codehong.convention.configureFlavors
import com.codehong.convention.getPluginId
import com.codehong.convention.getVersion
import com.codehong.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply(plugin = libs.getPluginId("android-application"))
            apply(plugin = libs.getPluginId("kotlin-android"))
            apply(plugin = libs.getPluginId("kotlin-kapt"))
            apply(plugin = libs.getPluginId("kotlin-parcelize"))
            apply(plugin = libs.getPluginId("ksp"))
            apply(plugin = libs.getPluginId("kotlin-compose-compiler"))

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    targetSdk = libs.getVersion("targetSdk")
                }
                configureAndroid(
                    commonExtension = this,
                    isCompose = true
                )
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
                configureFlavors(this, ExtensionType.APPLICATION)
            }
        }
    }
}