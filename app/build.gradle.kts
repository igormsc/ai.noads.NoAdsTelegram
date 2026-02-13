plugins {
    id("com.android.application") version "8.13.2"
    id("org.jetbrains.kotlin.android") version "2.2.10"
}

android {
    namespace = "ai.noads.telegram"
    compileSdk = 35

    defaultConfig {
        applicationId = "ai.noads.telegram"
        minSdk = 29
        targetSdk = 34
        versionCode = 6
        versionName = "0.666"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        applicationVariants.all {
            outputs.all {
                val variant = this
                if (variant is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                    variant.outputFileName = "NoAdsTelegram-v${versionName}.apk"
                }
            }
        }


    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lint {
        abortOnError = true
        checkReleaseBuilds = false
    }


}

dependencies {
    compileOnly(files("/NoAds/xposedbridge/api82/api-82.jar"))
}
