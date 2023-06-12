sealed class AndroidTestLibs(val name: String) {

    object testRunner : AndroidTestLibs("androidx.test:runner:1.2.0")
    object extJUnit : AndroidTestLibs("androidx.test.ext:junit:1.1.5")
    object espressoCore : AndroidTestLibs("androidx.test.espresso:espresso-core:3.5.1")
    object uiTestJUnit4 : AndroidTestLibs("androidx.compose.ui:ui-test-junit4:${ProjectVersions.composeVersion}")

    object jupiterApi : AndroidTestLibs("org.junit.jupiter:junit-jupiter-api:5.9.3")
    object jupiterParams : AndroidTestLibs("org.junit.jupiter:junit-jupiter-params:5.9.3")

    sealed class hamcrest(name: String) : AndroidTestLibs(name) {
        object core : hamcrest("org.hamcrest:hamcrest-core:2.2")
    }
}
