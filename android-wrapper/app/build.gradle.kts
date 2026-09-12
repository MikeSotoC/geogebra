plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.mikesotoc.geogebra"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mikesotoc.geogebra"
        minSdk = 24
        targetSdk = 35
        versionCode = 2
        versionName = "1.1-native"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    jvmToolchain(17)
}
