plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.example.app"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 25
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // The CI "minified APK" job runs instrumented tests against the R8
        // release artifact; AGP only registers androidTest tasks for the
        // testBuildType variant, so it must be release for
        // assembleReleaseAndroidTest / connectedReleaseAndroidTest to exist.
        testBuildType = "release"
    }

    val keystoreFile = rootProject.file("test_keystore.jks")

    // test_keystore.jks is git-ignored (*.jks), so CI cannot rely on it being
    // checked out. Regenerate it with the same alias/passwords the build
    // expects ("test"/123456) whenever it is missing, before any build task.
    val createTestKeystore by tasks.registering(Exec::class) {
        onlyIf { !keystoreFile.exists() }
        val keytoolBin = File(System.getProperty("java.home"), "bin/keytool")
        commandLine(
            if (keytoolBin.exists()) keytoolBin.absolutePath else "keytool",
            "-genkeypair", "-v",
            "-keystore", keystoreFile.absolutePath,
            "-storetype", "PKCS12",
            "-alias", "test",
            "-keyalg", "RSA", "-keysize", "2048", "-validity", "10000",
            "-storepass", "123456", "-keypass", "123456",
            "-dname", "CN=AppDimens Android CI, OU=CI, O=AppDimens, L=Unspecified, ST=Unspecified, C=BR"
        )
        outputs.file(keystoreFile)
    }
    tasks.matching { it.name == "preBuild" }.configureEach {
        dependsOn(createTestKeystore)
    }

    signingConfigs {
        create("sample") {
            storeFile = keystoreFile
            storePassword = System.getenv("SAMPLE_STORE_PASSWORD") ?: "123456"
            keyAlias = "test"
            keyPassword = System.getenv("SAMPLE_KEY_PASSWORD") ?: "123456"
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("sample")
        }
        debug {
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("sample")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
        resValues = true
    }
}

dependencies {
    api(project(":library"))

    //implementation("io.github.bodenberg:appdimens-ssps:3.1.6")
    // or
    //implementation("com.github.bodenberg.appdimens:appdimens-ssps:3.1.6")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.window)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}