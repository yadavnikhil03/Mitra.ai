plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.mitra.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mitra.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.2-beta"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = false
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig  = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.fragment)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.splashscreen)

    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)

    implementation(libs.coroutines.android)
    implementation(libs.work.runtime)
    implementation(libs.work.runtime.ktx)

    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}