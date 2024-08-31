// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion by extra("1.4.32")
    val hiltVersion by extra("2.44.2")

    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        val navVersion = "2.5.3"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.dagger.hilt.android") version "2.44.2" apply false
//    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}