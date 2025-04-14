// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    project.extra.set("composeVersion", "1.5.15")
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization) apply false
    alias(libs.plugins.google.ksp) apply false
}