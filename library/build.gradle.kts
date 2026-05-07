import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.kotlin.multiplatform.android.library)
}

group = "dev.zwander.compose.materialyou"

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

val javaVersionEnum: JavaVersion = JavaVersion.VERSION_21

kotlin {
    jvmToolchain(javaVersionEnum.toString().toInt())

    jvm {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget = JvmTarget.fromTarget(javaVersionEnum.toString())
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "MultiplatformMaterialYou"
            isStatic = true
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    listOf(
        js(IR),
        wasmJs(),
    ).forEach {
        it.outputModuleName.set("MultiplatformMaterialYou")
        it.browser()
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.addAll("-Xexpect-actual-classes", "-Xdont-warn-on-error-suppression")
                }
            }
        }
    }

    android {
        withJava()

        this.compileSdk = 36
        this.minSdk = 21

        namespace = "dev.zwander.compose.materialyou"

        compilerOptions {
            freeCompilerArgs.addAll("-opt-in=kotlin.RequiresOptIn", "-Xdont-warn-on-error-suppression")
            jvmTarget.set(JvmTarget.fromTarget(javaVersionEnum.toString()))
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.compose.runtime)
                api(libs.compose.ui)
                api(libs.kotlin.stdlib)
                api(libs.kotlin.reflect)
            }
        }

        val jvmMain by getting {
            dependsOn(commonMain)

            dependencies {
                api(libs.jsystemthemedetector)
                api(libs.jna)
                api(libs.jna.platform)
                api(libs.jfa)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }

        val iosX64Main by getting {
            dependsOn(iosMain)
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val macosMain by creating {
            dependsOn(commonMain)
        }

        val macosArm64Main by getting {
            dependsOn(macosMain)
        }

        val jsAndWasmMain by creating {
            dependsOn(commonMain)
        }

        val jsMain by getting {
            dependsOn(jsAndWasmMain)
        }

        val wasmJsMain by getting {
            dependsOn(jsAndWasmMain)
        }
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
