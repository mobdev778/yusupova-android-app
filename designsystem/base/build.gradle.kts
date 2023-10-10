plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.github.mobdev778.yusupova.designsystem.base"
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

        Libs.kotlin.coroutinesCore,

        Libs.androidX.compose.ui,
        Libs.androidX.compose.uiToolingPreview,
        Libs.androidX.compose.material3,

        Libs.timber
    )
}
