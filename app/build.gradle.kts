plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.monitoreo_silos"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.monitoreo_silos"
        minSdk = 24
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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // =====================================
    // NAVEGACIÓN
    // =====================================
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // =====================================
    // ROOM - SQLITE
    // =====================================
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // =====================================
    // JETPACK COMPOSE
    // =====================================
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("androidx.compose.material:material-icons-extended")

    // =====================================
    // VIEWMODEL - MVVM
    // =====================================
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // =====================================
    // CORE
    // =====================================
    implementation("androidx.core:core-ktx:1.15.0")

    // =====================================
    // IMÁGENES
    // =====================================
    implementation("io.coil-kt:coil-compose:2.6.0")

    // =====================================
    // TESTING
    // =====================================
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // =====================================
    // DEBUG
    // =====================================
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}