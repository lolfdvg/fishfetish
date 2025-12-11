plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.fish"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.fish"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true

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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Векторные изображения
    implementation("androidx.vectordrawable:vectordrawable:1.1.0")

    // JSON
    implementation("org.json:json:20231013")

    // Kotlin
    implementation(libs.kotlin.stdlib)

    // Дополнительные зависимости
    implementation(libs.recyclerview)
    implementation(libs.cardview)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    // Сеть (Retrofit + OkHttp)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)

    // Изображения
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Room для локальной БД
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Supabase
    implementation("io.github.jan-tennert.supabase:postgrest-kt:1.2.0")
    implementation("io.github.jan-tennert.supabase:gotrue-kt:1.2.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}