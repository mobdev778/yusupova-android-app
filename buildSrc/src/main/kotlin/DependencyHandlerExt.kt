import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(vararg libs: Libs) = libs.forEach {
    add("implementation", it.name)
}

fun DependencyHandler.implementation(vararg projectLibs: ProjectLibs) = projectLibs.forEach {
    val map = mapOf("path" to it.name)
    val projectDependency = project(map) as ProjectDependency
    add("implementation", projectDependency)
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
