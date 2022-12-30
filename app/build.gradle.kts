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
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation ("androidx.activity:activity-compose:1.3.1")
    implementation ("androidx.compose.ui:ui:${ProjectVersions.composeVersion}")
    implementation ("androidx.compose.ui:ui-tooling-preview:${ProjectVersions.composeVersion}")
    implementation ("androidx.compose.material3:material3:1.0.0-alpha02")
    implementation ("androidx.core:core-splashscreen:1.0.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:${ProjectVersions.composeVersion}")
    debugImplementation ("androidx.compose.ui:ui-tooling:${ProjectVersions.composeVersion}")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:${ProjectVersions.composeVersion}")
}

repositories {
    mavenCentral()
}