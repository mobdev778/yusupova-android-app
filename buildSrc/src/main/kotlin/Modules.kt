sealed class Modules(internal val name: String) {
    sealed class designSystem(name: String) : Modules(":designsystem$name") {
        object base : designSystem(":base")
        object animatedTextView : designSystem(":animatedtextview")
        object animatedSplashView : designSystem(":animatedsplashview")
    }

    object domain : Modules(":domain")

    sealed class repository(name: String) : Modules(":repository$name") {
        object appConfig : repository(":appconfig")
        object network : repository(":network")
        object verses : repository(":verses")
    }
}
