@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.movierama"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.movierama"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_API_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "BASE_API_KEY","\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0MjhkYjc0OGQ0ZDczOGEyMjhmNGM4OGVkYzA1YTI3NSIsInN1YiI6IjY0ODA5NWNmOTkyNTljMDBlMmYyZDljMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.T9BMoyeYnCgihW0iOXFBaEVyIPF6Z8BG3GiUKxmK8Lw\"")

            isMinifyEnabled = false
        }

        release {
            buildConfigField("String", "BASE_API_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "BASE_API_KEY","\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0MjhkYjc0OGQ0ZDczOGEyMjhmNGM4OGVkYzA1YTI3NSIsInN1YiI6IjY0ODA5NWNmOTkyNTljMDBlMmYyZDljMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.T9BMoyeYnCgihW0iOXFBaEVyIPF6Z8BG3GiUKxmK8Lw\"")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kapt {
        correctErrorTypes = true
    }

}

dependencies {

    implementation(project(":common"))
    implementation(libs.bundles.kotlin.main)

    // UI components
    implementation(libs.bundles.ui.components)

    // Lifecycle components
    implementation(libs.bundles.lifecycle.components)

    // Dagger - Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // Navigation
    implementation(libs.bundles.navigation)

    // Network (Retrofit)
    implementation(libs.bundles.network)

    // Local database (Room)
    implementation(libs.bundles.room)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)
    implementation(libs.hawk)

    // Image loading
    implementation(libs.glide)

    implementation(libs.timber)

    // Testing
    testImplementation(libs.bundles.testImplementationLibs)
    androidTestImplementation(libs.bundles.androidTestImplementationLibs)
}