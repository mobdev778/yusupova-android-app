plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.github.mobdev778.yusupova.designsystem.animatedsplashview"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectVersions.composeCompilerVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation (
        Libs.androidX.coreKtx,
        Libs.androidX.lifecycleRuntimeKtx,

        Libs.androidX.compose.ui,
        Libs.androidX.compose.uiToolingPreview,
        Libs.androidX.compose.material3,

        Libs.kotlin.coroutinesCore,

        Libs.timber
    )

    testImplementation(
        TestLibs.jUnit.jupiterApi
    )

    androidTestImplementation(
        AndroidTestLibs.extJUnit,
        AndroidTestLibs.espressoCore,
        AndroidTestLibs.uiTestJUnit4
    )

    debugImplementation(
        DebugLibs.androidX.compose.uiTooling,
        DebugLibs.androidX.compose.uiTestManifest
    )
}
