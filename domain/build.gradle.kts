plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.github.mobdev778.yusupova.domain"
    compileSdk = ProjectVersions.compileSdk

    defaultConfig {
        minSdk = ProjectVersions.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation (
        Libs.androidX.coreKtx,
        Libs.kotlin.coroutinesCore
    )
}
