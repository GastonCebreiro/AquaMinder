plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
//    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.aquaminder"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.aquaminder"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }


    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    val hiltVersion = "2.44.2"
    val navVersion = "2.5.3"
    val retrofitVersion = "2.4.0"
    val httpLoggingVersion = "3.12.0"
    val roomVersion = "2.4.0"

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

    // navHost
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    kapt("androidx.lifecycle:lifecycle-compiler:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.fragment:fragment-ktx:1.5.5")

    // Gson
    implementation("com.google.code.gson:gson:2.8.6")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:$httpLoggingVersion")

    // Room components
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")
    implementation("com.google.android.gms:play-services-location:19.0.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")

    implementation("androidx.recyclerview:recyclerview:1.2.1")

    implementation("androidx.security:security-crypto:1.1.0-rc01")

    implementation ("androidx.appcompat:appcompat:1.3.1")
}