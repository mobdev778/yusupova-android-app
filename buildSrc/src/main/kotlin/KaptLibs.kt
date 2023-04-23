sealed class KaptLibs(internal val name: String) {

    sealed class dagger2(name: String) : KaptLibs(name) {

        object daggerCompiler : dagger2("com.google.dagger:dagger-compiler:${ProjectVersions.daggerVersion}")
    }

    sealed class room(name: String) : KaptLibs(name) {

        object roomCompiler : room("androidx.room:room-compiler:${ProjectVersions.roomVersion}")
    }
}
