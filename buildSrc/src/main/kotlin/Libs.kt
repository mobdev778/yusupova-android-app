sealed class Libs(internal val name: String) {

    sealed class kotlin(dependencyNotation: String) : Libs(dependencyNotation) {
        object kotlinStdLibJdk8 : kotlin("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${ProjectVersions.kotlinVersion}")
        object coroutinesCore :
            kotlin("org.jetbrains.kotlinx:kotlinx-coroutines-core:${ProjectVersions.coroutinesVersion}")

        object coroutinesAndroid :
            kotlin("org.jetbrains.kotlinx:kotlinx-coroutines-android:${ProjectVersions.coroutinesVersion}")
    }

    sealed class androidX(name: String) : Libs(name) {

        object coreKtx : androidX("androidx.core:core-ktx:1.9.0")
        object coreSpashScreen : androidX("androidx.core:core-splashscreen:1.0.0")
        object lifecycleRuntimeKtx : androidX("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
        object activityCompose : androidX("androidx.activity:activity-compose:1.6.1")

        sealed class compose(name: String) : androidX(name) {
            object ui : compose("androidx.compose.ui:ui:${ProjectVersions.composeVersion}")
            object uiToolingPreview :
                compose("androidx.compose.ui:ui-tooling-preview:${ProjectVersions.composeVersion}")

            object material3 : compose("androidx.compose.material3:material3:1.1.0-alpha04")
            object navigation : compose("androidx.navigation:navigation-compose:2.5.0")
        }
    }

    sealed class dagger2(name: String) : Libs(name) {

        object dagger : dagger2("com.google.dagger:dagger:${ProjectVersions.daggerVersion}")
    }

    sealed class retrofit2(name: String) : Libs(name) {

        object retrofit : retrofit2("com.squareup.retrofit2:retrofit:2.9.0")
        object moshi : retrofit2("com.squareup.moshi:moshi-kotlin:1.9.0")
        object converterMoshi : retrofit2("com.squareup.retrofit2:converter-moshi:2.9.0")

    }

    sealed class okHttp3(name: String) : Libs(name) {

        object okhttp : okHttp3("com.squareup.okhttp3:okhttp:4.10.0")
        object loggingInterceptor : okHttp3("com.squareup.okhttp3:logging-interceptor:4.10.0")

    }

    sealed class chucker(dependencyNotation: String) : Libs(dependencyNotation) {
        object library : chucker("com.github.chuckerteam.chucker:library:3.5.2")
    }

    sealed class room(dependencyNotation: String) : Libs(dependencyNotation) {
        object runtime : room("androidx.room:room-runtime:${ProjectVersions.roomVersion}")
        object ktx : room("androidx.room:room-ktx:${ProjectVersions.roomVersion}")
    }

    object timber : Libs("com.jakewharton.timber:timber:5.0.1")
}
