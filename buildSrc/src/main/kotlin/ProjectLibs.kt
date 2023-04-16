
sealed class ProjectLibs(internal val name: String) {
    object animatedTextView: ProjectLibs(":animatedtextview")
    object animatedSplashView: ProjectLibs(":animatedsplashview")
}
