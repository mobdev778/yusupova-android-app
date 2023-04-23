plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.github.mobdev778.yusupova.animatedsplashview"
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
        kotlinCompilerExtensionVersion = "1.3.2"
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

        Libs.timber
    )

    testImplementation(
        TestLibs.jUnit
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
