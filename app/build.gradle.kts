plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ara.aranote"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "220.904"

        testInstrumentationRunnerArguments["disableAnalytics"] = "true"
        testInstrumentationRunner = "com.ara.aranote.HTestRunner"

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            isDebuggable = true
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true

        // Disable unused AGP features
        aidl = false
        renderScript = false
        resValues = false
        shaders = false
//        buildConfig = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packagingOptions {
        resources {
            // Multiple dependency bring these files in. Exclude them to enable
            // our test APK to build (has no effect on our AARs)
            excludes += "/META-INF/AL2.0"
            excludes += "/META-INF/LGPL2.1"

            // https://github.com/Kotlin/kotlinx.coroutines#avoiding-including-the-debug-infrastructure-in-the-resulting-apk
            excludes += "DebugProbesKt.bin"
        }
    }

    lint {
        checkDependencies = true
    }

    // Tests can be Robolectric or instrumented tests
    sourceSets {
        val sharedTestDir = "src/test_common/java"
//        getByName("test") {
//            java.srcDir(sharedTestDir)
//        }
        getByName("androidTest") {
            java.srcDir(sharedTestDir)
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
        animationsDisabled = true
    }

    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }
}

tasks.withType<Test>().configureEach {
    systemProperties.put("robolectric.logging", "stdout")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        // Treat all Kotlin warnings as errors (disabled by default)
        allWarningsAsErrors = if (project.hasProperty("warningsAsErrors"))
            project.findProperty("warningsAsErrors")!! as Boolean
        else false

        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"

        // Set JVM target to 1.8
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":core"))

    testImplementation(project(":core_test"))
    androidTestImplementation(project(":core_test"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // ### Jetpack Compose ####
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    // Tooling support (Previews, etc.)
    debugImplementation(libs.androidx.compose.ui.tooling)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(libs.androidx.compose.foundation)
    // Material Design
    implementation(libs.androidx.compose.material)
    // Material design icons
    implementation(libs.androidx.compose.material.iconsExtended)
    // Integration with activities
    implementation(libs.androidx.activity.compose)
    // Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewModelCompose)
    // Compose Testing
    testImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.compose.ui.test)
    // Test rules and transitive dependencies:
    testImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Needed for createComposeRule, but not createAndroidComposeRule:
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    // Animation
    implementation(libs.androidx.compose.animation)
    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.accompanist.navigation.animation)
//    androidTestImplementation "androidx.navigation:navigation-testing:$navigation_compose"

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    // Hilt Testing
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    // Room DB
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.room.testing)

    // DateTime
    coreLibraryDesugaring(libs.core.jdk.desugaring)
    implementation(libs.kotlinx.datetime)
    implementation(libs.prettytime)

    // Kotlinx Coroutines
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.kotlinx.coroutines.test)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging)

    // ### Unit and Instrumented Test ###
    testImplementation(libs.junit)
//    androidTestImplementation(libs.junit)
    testImplementation(libs.mockk)
//    testImplementation(libs.mockk.agent.api)
    testImplementation(libs.mockk.agent.jvm)
    testImplementation(libs.kotlin.reflect)
    // Androidx Test Core
    testImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.test.core.ktx)
    // AndroidJUnitRunner and JUnit Rules
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    // Assertions
    testImplementation(libs.androidx.test.ext.junit.ktx)
    androidTestImplementation(libs.androidx.test.ext.junit.ktx)
    androidTestImplementation(libs.androidx.test.ext.truth)
    testImplementation(libs.google.truth)
    androidTestImplementation(libs.google.truth)
    // Espresso dependencies
    testImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.espresso.core)
    // Other Test Libs
    testImplementation(libs.robolectric)

    // Other Libs
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.datastore)
    implementation(libs.timber)
    implementation(libs.karn.notify)
    implementation(libs.afollestad.material.dialogs.color)
    implementation(libs.coil.kt.compose)
}
