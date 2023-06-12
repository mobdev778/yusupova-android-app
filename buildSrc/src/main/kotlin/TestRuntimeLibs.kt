sealed class TestRuntimeLibs(val name: String) {

    sealed class jUnit(name: String): TestRuntimeLibs(name) {
        object jupiterEngine : jUnit("org.junit.jupiter:junit-jupiter-engine:5.9.3")
        object vintageEngine : jUnit("org.junit.vintage:junit-vintage-engine:5.3.2")
    }
}
