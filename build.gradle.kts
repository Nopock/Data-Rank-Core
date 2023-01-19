import org.jetbrains.gradle.ext.packagePrefix
import org.jetbrains.gradle.ext.settings
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.intellij.settings)
    alias(libs.plugins.shadowjar)
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("plugin.spring") version "1.6.21"
}

group = "org.hyrical"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://libraries.minecraft.net/")
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.serialization.core)
    implementation(libs.kotlin.serialization.yaml)
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("co.aikar:acf-bukkit:0.5.1-SNAPSHOT")
}

idea {
    module {
        settings {
            for (sourceSet in kotlin.sourceSets)
                for (sourceDir in sourceSet.kotlin.sourceDirectories)
                    packagePrefix[sourceDir.toRelativeString(projectDir)] = "org.hyrical.data"
        }
    }
}

tasks {
    shadowJar {
        archiveFileName.set("data-core.jar")
        relocate("io.netty", "org.hyrical.data.relocated.io.netty")
        relocate("co.aikar.commands", "org.hyrical.data.commands")
        relocate("co.aikar.locales", "org.hyrical.data.locales")
    }
}