import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    signingConfigs {
        create("release") { }
    }
    namespace = "com.feko.generictabletoprpg"
    compileSdk = 36
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "com.feko.generictabletoprpg"
        minSdk = 24
        targetSdk = 36
        versionCode = 8
        versionName = libs.versions.app.get()

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs.getByName("release") {
        storeFile = File("${project.rootDir}/build-data/release.jks")
        storePassword = providers.gradleProperty("storePassword").get()
        keyAlias = providers.gradleProperty("keyAlias").get()
        keyPassword = providers.gradleProperty("keyPassword").get()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    sourceSets["main"].res.srcDirs("../shared/src/commonMain/composeResources")
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_19)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation(project(":shared"))
}