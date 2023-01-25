
sealed class Libs(internal val name: String) {

    sealed class androidX(name: String): Libs(name) {

        object coreKtx: androidX("androidx.core:core-ktx:1.9.0")
        object coreSpashScreen: androidX("androidx.core:core-splashscreen:1.0.0")
        object lifecycleRuntimeKtx: androidX("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
        object activityCompose: androidX("androidx.activity:activity-compose:1.6.1")

        sealed class compose(name: String): androidX(name) {
            object ui: compose("androidx.compose.ui:ui:${ProjectVersions.composeVersion}")
            object uiToolingPreview: compose("androidx.compose.ui:ui-tooling-preview:${ProjectVersions.composeVersion}")
            object material3: compose("androidx.compose.material3:material3:1.1.0-alpha04")
        }
    }

    object timber: Libs("com.jakewharton.timber:timber:5.0.1")
}
