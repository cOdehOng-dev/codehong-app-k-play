import java.util.Properties




val githubProperties = Properties().apply {
    load(file("github.properties").inputStream())
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri(githubProperties.getProperty("url"))
            credentials {
                username = githubProperties.getProperty("username")
                password = githubProperties.getProperty("token")
            }
        }
        maven("https://repository.map.naver.com/archive/maven")
    }
    versionCatalogs {
//        create("libs") {
//            from(files("gradle/libs.versions.toml"))
//        }
        create("codehonglibs") {
            from(files("gradle/codehonglibs.versions.toml"))
        }
    }
}


enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "code-hong-app-k-play"
include(":app")
//include(":Widget")
