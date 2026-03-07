// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.ksp) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

fun cleanAfterRelease(task: Task) {
    task.doLast {
        println("🚀 Release concluído para: ${task.name}")
        // Buscamos especificamente o subprojeto "app"
        val appProject = childProjects["app"]
        appProject?.let { proj ->
            val buildDir = proj.layout.buildDirectory.asFile.get()
            println("🧹 Limpando módulo :app em ${buildDir.path}")
            buildDir.listFiles()?.forEach { file ->
                if (file.name != "outputs") {
                    file.deleteRecursively()
                }
            }
        }
        println("✨ Limpeza do módulo :app concluída.")
    }
}

tasks.matching {
    it.name == ":app:assembleRelease" || it.name == ":app:bundleRelease"
}.configureEach {
    cleanAfterRelease(this)
}