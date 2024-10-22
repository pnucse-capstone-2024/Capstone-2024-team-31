// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.java.net/content/groups/public/")
        }
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.kotlin.serialization)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.gms) apply false
}
