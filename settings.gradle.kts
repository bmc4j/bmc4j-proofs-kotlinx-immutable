// Repositories: GitHub Packages is bmc4j's pre-Central snapshot channel — it needs SOME authenticated
// token with read:packages even for public packages (in CI the workflow's own GITHUB_TOKEN; locally
// `gh auth token` if it has the scope, or -Pgpr.user/-Pgpr.token). mavenLocal first so a local
// `publishToMavenLocal` of bmc4j also resolves.
pluginManagement {
    repositories {
        mavenLocal()
        maven {
            name = "Bmc4jGitHubPackages"
            url = uri("https://maven.pkg.github.com/bmc4j/bmc4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN") ?: providers.gradleProperty("gpr.token").orNull
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            name = "Bmc4jGitHubPackages"
            url = uri("https://maven.pkg.github.com/bmc4j/bmc4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN") ?: providers.gradleProperty("gpr.token").orNull
            }
        }
        mavenCentral()
    }
}

rootProject.name = "bmc4j-proofs-kotlinx-immutable"
