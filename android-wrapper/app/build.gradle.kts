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
        versionCode = 1
        versionName = "1.0"
    }
}
