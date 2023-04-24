import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "moe.peanutmelonseedbigalmond.kemonodl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.github.haroldadmin:NetworkResponseAdapter:4.2.2")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation(project(":aria2"))


    implementation("org.hibernate:hibernate-core:6.1.4.Final")
    implementation("com.h2database:h2:2.1.212")

    implementation("com.thoughtworks.xstream:xstream:1.4.20")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("moe.peanutmelonseedbigalmond.kemondl.MainKt")
}