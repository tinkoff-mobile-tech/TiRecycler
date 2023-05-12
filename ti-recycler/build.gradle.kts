plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}

apply(from = "../gradle/publish-lib.gradle")


android {
    namespace = "ru.tinkoff.mobile.tech.ti_recycler"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    api("androidx.core:core-ktx:1.10.0")
    api("androidx.recyclerview:recyclerview:1.3.0")
}
