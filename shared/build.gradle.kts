plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.feko.generictabletoprpg.shared"
        compileSdk = 36
        minSdk = 24
        androidResources.enable = true

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    jvm("desktop")

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.bpsm.edn.java)
                implementation(libs.bundles.compose.mp)
                implementation(libs.bundles.compose.mp.material)
                implementation(libs.bundles.file.kit)
                implementation(libs.bundles.navigation3)
                implementation(libs.fuzzywuzzy.kotlin)
                implementation(libs.jetbrains.kotlinx.serialization.json)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.kotlin.logging)
                implementation(libs.kotlin.stdlib)
                implementation(libs.lifecycle.viewmodel.compose)
                implementation(libs.multiplatform.settings)
                implementation(libs.reorderable)
                implementation(libs.sonner)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.room.ktx)
                implementation(libs.slf4j.android)
            }
        }

        val desktopMain by getting
        desktopMain.dependencies {
            implementation(libs.compose.desktop)
            implementation(compose.desktop.currentOs)
            implementation(libs.coroutines.swing)
            implementation(libs.slf4j.simple)
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.junit)
                implementation(libs.androidx.test.runner)
                implementation(libs.jetbrains.kotlinx.coroutines.test)
                implementation(libs.slf4j.android)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

compose.resources {
    publicResClass = false
    packageOfResClass = "com.feko.generictabletoprpg"
    generateResClass = auto
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
}
