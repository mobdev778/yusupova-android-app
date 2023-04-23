sealed class Modules(internal val name: String) {
    sealed class designSystem(name: String) : Modules(":designsystem$name") {
        object animatedTextView : designSystem(":animatedtextview")
        object animatedSplashView : designSystem(":animatedsplashview")
    }

    object domain : Modules(":domain")
}
