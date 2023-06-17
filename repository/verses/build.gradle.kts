plugins {
    id("com.android.library")
    id("de.mannodermaus.android-junit5")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.github.mobdev778.yusupova.repository.verses"
    compileSdk = ProjectVersions.compileSdk

    defaultConfig {
        minSdk = ProjectVersions.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
        }
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
        Modules.domain,
        Modules.repository.network
    )

    implementation (
        Libs.dagger2.dagger,

        Libs.retrofit2.retrofit,
        Libs.retrofit2.moshi,
        Libs.retrofit2.converterMoshi,

        Libs.kotlin.coroutinesCore,

        Libs.room.runtime,
        Libs.room.ktx,

        Libs.timber,

        Libs.androidX.test.coreKtx
    )

    testImplementation(
        TestLibs.jUnit.jupiterApi,
        TestLibs.jUnit.jupiterParams,
    )

    androidTestImplementation(project(":testing"))

    androidTestImplementation(
        AndroidTestLibs.testRunner,
        AndroidTestLibs.jupiterApi,
        AndroidTestLibs.jupiterParams,
        AndroidTestLibs.hamcrest.core,
        AndroidTestLibs.okHttp3.loggingInterceptor
    )

    testRuntimeOnly(
        TestRuntimeLibs.jUnit.jupiterEngine,
        TestRuntimeLibs.jUnit.vintageEngine
    )

    kapt(
        KaptLibs.dagger2.daggerCompiler,
        KaptLibs.room.roomCompiler
    )

    kaptAndroidTest(
        KaptLibs.dagger2.daggerCompiler
    )
}
