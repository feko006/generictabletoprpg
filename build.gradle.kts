// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    project.extra.set("composeVersion", "1.5.3")
}

plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.android.application) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.google.ksp) apply false
}