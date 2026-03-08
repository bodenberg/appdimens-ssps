import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.vanniktech.maven.publish)
    // alias(libs.plugins.dokka.jetbrains)
}

val isJitPack = System.getenv("JITPACK") == "true"
        || System.getenv("jitpack") == "true"
        || System.getenv("CI") == "true"
        || System.getenv("ci") == "true"

mavenPublishing {
    coordinates("io.github.bodenberg", "appdimens-ssps", "3.0.0")

    configure(
        AndroidSingleVariantLibrary(
            publishJavadocJar = true,
            sourcesJar = true
        )
    )

    pom {
        name.set("AppDimens SSP, HSP, WSP")
        description.set("Scalable width and height fonts for Android layouts")
        url.set("https://github.com/bodenberg/appdimens-ssps")

        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("bodenberg")
                name.set("Jean Bodenberg")
                email.set("jean.bodenberg2@outlook.com")
            }
        }

        scm {
            connection.set("scm:git:github.com/bodenberg/appdimens-ssps.git")
            developerConnection.set("scm:git:ssh://github.com/bodenberg/appdimens-ssps.git")
            url.set("https://github.com/bodenberg/appdimens-ssps")
        }
    }

    if (!isJitPack) {
        publishToMavenCentral()
        signAllPublications()
    }
}

/* dokka {
    dokkaPublications.html {
        moduleName.set("AppDimens SDP, HDP, WDP: Scalable Width and Height Dimensions")
        outputDirectory.set(layout.projectDirectory.dir("${rootDir}\\DOCUMENTATION"))
        suppressInheritedMembers.set(true)
        dokkaSourceSets.register("main") {
            sourceRoots.from(file("src/main/java"), file("src/main/kotlin"))
            documentedVisibilities.set(
                setOf(
                    VisibilityModifier.Public,
                    VisibilityModifier.Internal,
                    VisibilityModifier.Protected,
                    VisibilityModifier.Private,
                    VisibilityModifier.Package
                )
            )
            skipEmptyPackages.set(false)
            skipDeprecated.set(false)
            reportUndocumented.set(false)
            enableAndroidDocumentationLink.set(true)
            enableJdkDocumentationLink.set(true)
            enableKotlinStdLibDocumentationLink.set(true)
            failOnWarning.set(false)
            val localPropsFile = rootProject.file("local.properties")
            val sdkDirFromLocal: String? = if (localPropsFile.exists()) {
                val props = Properties()
                props.load(localPropsFile.inputStream())
                props.getProperty("sdk.dir")
            } else null
            val sdkDir = sdkDirFromLocal ?: System.getenv("ANDROID_SDK_ROOT") ?: System.getenv("ANDROID_HOME")
            val compileSdkVersion = try {
                android.compileSdk.toString()
            } catch (ignored: Exception) {
                null
            }
            if (sdkDir != null && compileSdkVersion != null) {
                val androidJarPath = file("${sdkDir}/platforms/android-$compileSdkVersion/android.jar")
                if (androidJarPath.exists()) {
                    classpath.from(files(androidJarPath))
                    logger.lifecycle("Dokka: added android.jar to classpath: $androidJarPath")
                } else {
                    logger.warn("Dokka: android.jar not found at $androidJarPath — Dokka pode continuar com símbolos não resolvidos.")
                }
            } else {
                logger.warn("Dokka: Android SDK não encontrado (local.properties/sdk.dir ou ANDROID_SDK_ROOT/ANDROID_HOME) ou compileSdk não disponível.")
            }
            val compileCp = configurations.findByName("compileClasspath") ?: configurations.getByName("debugCompileClasspath")
            classpath.from(compileCp)
            configurations.findByName("releaseRuntimeClasspath")?.let { classpath.from(it) }
            externalDocumentationLinks {
                create("android") {
                    url.set(URI("https://developer.android.com/reference/"))
                    packageListUrl.set(URI("https://developer.android.com/reference/package-list"))
                }
                create("androidx") {
                    url.set(URI("https://developer.android.com/reference/androidx/"))
                    packageListUrl.set(URI("https://developer.android.com/reference/androidx/package-list"))
                }
            }
        }
        pluginsConfiguration.html {
            footerMessage.set("Bodenberg")
            homepageLink.set("https://github.com/bodenberg")
        }
    }
}

tasks.withType<DokkaGenerateTask>().configureEach {
    doFirst {
        System.setProperty("java.awt.headless", "true")
    }
} */

android {
    namespace = "com.appdimens.ssps"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.get()
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.runtime)
    //dokkaPlugin(libs.android.documentation.plugin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}