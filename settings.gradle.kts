import java.util.Properties

val githubProperties = Properties().apply {
    load(file("github.properties").inputStream())
}

pluginManagement {
    includeBuild("code-hong-submodule-build-logic/build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://repository.map.naver.com/archive/maven")

        maven {
            url = uri(githubProperties.getProperty("url"))
            credentials {
                username = githubProperties.getProperty("username")
                password = githubProperties.getProperty("token")
            }
        }
    }
    versionCatalogs {
        create("libs") {
            from(files("build-logic/gradle/libs.versions.toml"))
        }
        create("codehonglibs") {
            from(files("gradle/codehonglibs.versions.toml"))
        }
    }
}

rootProject.name = "code-hong-app-k-play"
include(":app")
include(":domain")
//include(":Widget")
