import java.io.ByteArrayOutputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.search_from_share"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.search_from_share"
        minSdk = 24
        targetSdk = 34
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.browser:browser:1.8.0") // Custom Tabs
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

fun getVersionName(): String {
    return try {
        val stdout = ByteArrayOutputStream()
        project.exec {
            commandLine("git", "describe", "--tags", "--abbrev=0")
            standardOutput = stdout
        }
        stdout.toString().trim().removePrefix("v")
    } catch (e: Exception) {
        "0.0.1"
    }
}

fun getVersionCode(): Int {
    val versionName = getVersionName()
    val parts = versionName.split(".")
    if (parts.size < 3) return 1
    return try {
        parts[0].toInt() * 10000 + parts[1].toInt() * 100 + parts[2].toInt()
    } catch (e: Exception) {
        1
    }
}
