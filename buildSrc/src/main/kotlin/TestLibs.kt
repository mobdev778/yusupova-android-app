sealed class TestLibs(val name: String) {

    sealed class jUnit(name: String) : TestLibs(name) {
        object jupiterApi : jUnit("org.junit.jupiter:junit-jupiter-api:5.9.3")
        object jupiterParams : jUnit("org.junit.jupiter:junit-jupiter-params:5.9.3")
    }

    sealed class androidX(name: String) : TestLibs(name) {
        sealed class test(name: String) : androidX(name) {
            object coreKtx : test("androidx.test:core-ktx:1.5.0")
        }
    }
}
