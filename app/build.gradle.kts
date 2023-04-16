plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.github.mobdev778.yusupova"
    compileSdk = ProjectVersions.compileSdk

    defaultConfig {
        applicationId = "com.github.mobdev778.yusupova"
        minSdk = ProjectVersions.minSdk
        targetSdk = ProjectVersions.targetSdk
        versionCode = ProjectVersions.versionCode
        versionName = ProjectVersions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        val options = this
        options.jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    implementation (
        ProjectLibs.animatedTextView,
        ProjectLibs.animatedSplashView
    )

    implementation (
        Libs.androidX.coreKtx,
        Libs.androidX.lifecycleRuntimeKtx,
        Libs.androidX.activityCompose,

        Libs.androidX.compose.ui,
        Libs.androidX.compose.uiToolingPreview,
        Libs.androidX.compose.material3,

        Libs.androidX.coreSpashScreen,

        Libs.timber
    )

    testImplementation (
        TestLibs.jUnit
    )

    androidTestImplementation (
        AndroidTestLibs.extJUnit,
        AndroidTestLibs.espressoCore,
        AndroidTestLibs.uiTestJUnit4
    )

    debugImplementation(
        DebugLibs.androidX.compose.uiTooling,
        DebugLibs.androidX.compose.uiTestManifest
    )
}

repositories {
    mavenCentral()
}
