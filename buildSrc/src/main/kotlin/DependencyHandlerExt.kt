import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(vararg libs: Libs) = libs.forEach {
    add("implementation", it.name)
}

fun DependencyHandler.implementation(vararg modules: Modules) = modules.forEach {
    val map = mapOf("path" to it.name)
    val projectDependency = project(map) as ProjectDependency
    add("implementation", projectDependency)
}

fun DependencyHandler.kapt(vararg kaptLibs: KaptLibs) = kaptLibs.forEach {
    add("kapt", it.name)
}

fun DependencyHandler.kaptAndroidTest(vararg kaptLibs: KaptLibs) = kaptLibs.forEach {
    add("kaptAndroidTest", it.name)
}

fun DependencyHandler.testImplementation(vararg testLibs: TestLibs) = testLibs.forEach {
    add("testImplementation", it.name)
}

fun DependencyHandler.androidTestImplementation(vararg androidTestLibs: AndroidTestLibs) = androidTestLibs.forEach {
    add("androidTestImplementation", it.name)
}

fun DependencyHandler.debugImplementation(vararg debugLibs: DebugLibs) = debugLibs.forEach {
    add("debugImplementation", it.name)
}

fun DependencyHandler.testRuntimeOnly(vararg testLibs: TestRuntimeLibs) = testLibs.forEach {
    add("testRuntimeOnly", it.name)
}

fun DependencyHandler.api(vararg libs: Libs) = libs.forEach {
    add("api", it.name)
}

fun DependencyHandler.api(vararg testLibs: TestLibs) = testLibs.forEach {
    add("api", it.name)
}
