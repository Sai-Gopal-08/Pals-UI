pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Pals UI"
include(":app")
include(":components:animated_text")
include(":components:context_menu_card")
include(":components:flexible_icon")
include(":components:styled_switch")
include(":components:edge_accent_card")
include(":components:label_badge")
include(":components:stage_indicator")
include(":components:pill_tab_row")
