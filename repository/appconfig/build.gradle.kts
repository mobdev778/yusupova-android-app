plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.github.mobdev778.yusupova.repository.appconfig"
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
        Modules.domain
    )

    implementation (
        Libs.dagger2.dagger,

        Libs.androidX.coreKtx,
        Libs.androidX.lifecycleRuntimeKtx,

        Libs.androidX.compose.ui,
        Libs.androidX.compose.uiToolingPreview,
        Libs.androidX.compose.material3,

        Libs.timber
    )

    kapt(
        KaptLibs.dagger2.daggerCompiler
    )
}
