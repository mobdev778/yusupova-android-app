plugins {
    id("com.android.application")
    id("de.mannodermaus.android-junit5")
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
        getByName("debug") {
            applicationIdSuffix = "debug"
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.getByName("debug")
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
        kotlinCompilerExtensionVersion = ProjectVersions.composeCompilerVersion
    }
    packagingOptions {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    implementation (
        Modules.designSystem.base,
        Modules.designSystem.animatedTextView,
        Modules.designSystem.animatedSplashView,

        Modules.domain,

        Modules.repository.appConfig,
        Modules.repository.network,
        Modules.repository.verses
    )

    implementation (
        Libs.dagger2.dagger,

        Libs.retrofit2.retrofit,

        Libs.androidX.coreKtx,
        Libs.androidX.lifecycleRuntimeKtx,
        Libs.androidX.activityCompose,

        Libs.androidX.compose.ui,
        Libs.androidX.compose.uiToolingPreview,
        Libs.androidX.compose.material3,
        Libs.androidX.compose.navigation,

        Libs.androidX.coreSpashScreen,

        Libs.timber
    )

    kapt(
        KaptLibs.dagger2.daggerCompiler
    )

    testImplementation (
        TestLibs.jUnit.jupiterApi,
        TestLibs.jUnit.jupiterParams
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
