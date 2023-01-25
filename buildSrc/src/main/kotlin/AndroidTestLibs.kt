sealed class AndroidTestLibs(val name: String) {

    object extJUnit: AndroidTestLibs("androidx.test.ext:junit:1.1.5")
    object espressoCore: AndroidTestLibs("androidx.test.espresso:espresso-core:3.5.1")
    object uiTestJUnit4: AndroidTestLibs("androidx.compose.ui:ui-test-junit4:${ProjectVersions.composeVersion}")
}
