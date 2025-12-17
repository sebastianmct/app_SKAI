import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
}

group = "com.example"
version = "1.0.0"

java {
    // Ajustado para coincidir con la versiï¿½n de Java con la que estï¿½s ejecutando
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // Spring Boot Data JPA (solo para User, Order, Cart)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    
    // Driver JDBC de Oracle actualizado. Este ï¿½nico artefacto incluye las dependencias de seguridad necesarias.
    implementation("com.oracle.database.jdbc:ojdbc11:23.4.0.24.05")
    
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    
    // Jackson para JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    
    // Spring Boot DevTools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    
    // Spring Boot Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        // Ajustado para coincidir con la versiï¿½n de Java con la que estï¿½s ejecutando
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
