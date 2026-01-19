import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// Load local.properties for sensitive/local config
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

android {
    namespace = "com.nkechinnaji.speakquest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nkechinnaji.speakquest"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {

        }
    }

    flavorDimensions += "version"

    productFlavors {
        create("dev") {
            dimension = "version"
            applicationIdSuffix = ".dev"       // makes package unique
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", "\"https://dev.example.com/\"")

            val libreTranslateUrl = localProperties.getProperty("LIBRETRANSLATE_URL", "http://10.0.2.2:5000/")
            val libreTranslateApiKey = localProperties.getProperty("LIBRETRANSLATE_API_KEY", "")
            buildConfigField("String", "LIBRETRANSLATE_URL", "\"$libreTranslateUrl\"")
            buildConfigField("String", "LIBRETRANSLATE_API_KEY", "\"$libreTranslateApiKey\"")
        }
        create("prod") {
            dimension = "version"
            buildConfigField("String", "BASE_URL", "\"https://api.example.com/\"")
            buildConfigField("String", "LIBRETRANSLATE_URL", "\"https://libretranslate.com/\"")
            buildConfigField("String", "LIBRETRANSLATE_API_KEY", "\"\"")  // Add your key here
        }
    }

    android.buildFeatures.buildConfig = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}