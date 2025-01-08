import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
    id("androidx.room")
}

android {
    namespace = "com.abizer_r.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }


    val localProperties = Properties().apply {
        file("../local.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
    }
    val newsApiKey: String = localProperties.getProperty("NEWS_API_KEY")
        ?: "c0515b0da7f54bcd993091bbd5232e56"   // putting API key so that this project can run directly

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "NEWS_API_KEY", "\"$newsApiKey\"")
        }
        debug {
            buildConfigField("String", "NEWS_API_KEY", "\"$newsApiKey\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")


    // hilt
    implementation(libs.google.hilt.android)
    kapt(libs.google.hilt.compiler)
    implementation(libs.androidx.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}