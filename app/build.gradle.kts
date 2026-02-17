plugins {
    id("com.android.application") version "8.13.2"
}

android {
    namespace = "ai.noads.telegram"
    compileSdk = 35

    defaultConfig {
        applicationId = "ai.noads.telegram"
        minSdk = 29
        targetSdk = 34
        versionCode = 8
        versionName = "0.8"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
           // isShrinkResources = true
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
    compileOnly(files("/repo/NoAds/xposedbridge/api82/api-82.jar"))
}
