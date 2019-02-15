object ApplicationId {
    val id = "com.masauve.countries"
}

object Modules {
    // Base
    val app = ":app"

    // Common
    val networking = ":networking"
    val mvi = ":mvi"
    val androidmvi = ":androidmvi"

    // Features
    val countries = ":countrylist"
}

object Releases {
    val versionCode = 1
    val versionName = "1.0"
}

object Versions {
    val gradle = "3.3.1"

    val compileSdk = 28
    val minSdk = 21
    val targetSdk = 28

    val supportLibraries = "28.0.0"

    val appCompat = supportLibraries
    val design = supportLibraries
    val recyclerView = supportLibraries
    val constraintLayout = "1.1.3"

    val anko = "0.10.8"

    val kotlin = "1.3.21"
    val rxJava = "2.2.6"
    val rxKotlin = "2.3.0"
    val rxAndroid = "2.1.0"
    val rxBinding = "2.2.0"
    val retrofit = "2.5.0"
    val moshi = "1.8.0"
    val koin = "2.0.0-beta-1"
}

object Libraries {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    val gradle = "com.android.tools.build:gradle:${Versions.gradle}"

    val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    val rxBinding = "com.jakewharton.rxbinding2:rxbinding-kotlin:${Versions.rxBinding}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val rxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"

    val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"

    val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    val koinViewModel = "org.koin:koin-android-viewmodel:${Versions.koin}"
}

object SupportLibraries {
    val v4 = "com.android.support:support-v4:${Versions.supportLibraries}"
    val appCompat = "com.android.support:appcompat-v7:${Versions.appCompat}"
    val recyclerView = "com.android.support:recyclerview-v7:${Versions.recyclerView}"
    val design = "com.android.support:design:${Versions.design}"
    val constraintLayout = "com.android.support.constraint:constraint-layout:${Versions.constraintLayout}"
}

object AnkoLibraries {
    val layouts = "org.jetbrains.anko:anko-sdk25:${Versions.anko}"
    val appcompat = "org.jetbrains.anko:anko-appcompat-v7:${Versions.anko}"
    val design = "org.jetbrains.anko:anko-design:${Versions.anko}"
    val recyclerView = "org.jetbrains.anko:anko-recyclerview-v7:${Versions.anko}"
    val constraintLayout = "org.jetbrains.anko:anko-constraint-layout:${Versions.anko}"
}
