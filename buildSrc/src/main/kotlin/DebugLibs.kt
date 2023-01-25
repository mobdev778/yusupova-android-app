sealed class DebugLibs(val name: String) {

    sealed class androidX(name: String): DebugLibs(name) {

        sealed class compose(name: String): androidX(name) {

            object uiTooling: compose(
                "androidx.compose.ui:ui-tooling:${ProjectVersions.composeVersion}"
            )
            object uiTestManifest: compose(
                "androidx.compose.ui:ui-test-manifest:${ProjectVersions.composeVersion}"
            )
        }
    }
}
