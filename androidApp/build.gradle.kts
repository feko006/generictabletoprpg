plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.feko.generictabletoprpg"
    compileSdk {
        version = release(libs.versions.compileSdkVersion.get().toInt())
    }

    signingConfigs {
        create("release") { }
    }

    defaultConfig {
        applicationId = "com.feko.generictabletoprpg"
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.targetSdkVersion.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    sourceSets["main"].res.directories.add("../shared/src/commonMain/composeResources")
}

dependencies {
    implementation(project(":shared"))
}