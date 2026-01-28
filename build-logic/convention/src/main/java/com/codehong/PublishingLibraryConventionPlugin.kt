
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

class PublishingLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("maven-publish")

            task("publishReleaseToGitHub") {
                group = "github"
                dependsOn("assembleRelease")
                finalizedBy("publishReleasePublicationToGitHubPackagesRepository")
            }

            task("publishSnapshotToGitHub") {
                group = "github"
                dependsOn("assembleRelease")
                finalizedBy("publishSnapshotPublicationToGitHubPackagesRepository")
            }


            if (!project.plugins.hasPlugin("com.android.application")) {
                extensions.configure<PublishingExtension> {
                    repositories {
                        maven {
                            setUrl("https://repository.map.naver.com/archive/maven")
                        }
                        maven {
                            val props = Properties().apply {
                                load(rootProject.file("github.properties").inputStream())
                            }

                            name = "GitHubPackages"
                            url = uri(props.getProperty("url"))
                            credentials {
                                username = props.getProperty("username")
                                password = props.getProperty("token")
                            }
                        }
                    }

                    publications {
                        val groupId = project.properties["GROUP_ID"] as String
                        val artifactId = project.properties["ARTIFACT_ID"] as String
                        val artifact = project.properties["ARTIFACT"] as String
                        val version = project.properties["VERSION_NAME"] as String

                        register<MavenPublication>("release") {
                            this.groupId = groupId
                            this.artifactId = artifactId
                            this.version =  version
                            afterEvaluate {
                                artifact(artifact)
                            }
                        }

                        register<MavenPublication>("snapshot") {
                            this.groupId = groupId
                            this.artifactId = artifactId
                            this.version =  version +
                                    "-${SimpleDateFormat("yyyyMMdd.HHmmss").format(Date())}" +
                                    "-SNAPSHOT"
                            afterEvaluate {
                                artifact(artifact)
                            }
                        }
                    }
                }
            }
        }
    }
}
