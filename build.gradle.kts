import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val ktorVersion = "2.3.13"
val kafkaVersion = "3.3.1"
val jacksonVersion = "2.22.3"
val jacksonAnnotationsVersion = "2.22"
val konfigVersion = "1.6.10.0"
val kotlinLoggerVersion = "1.12.5"
val resilience4jVersion = "1.7.1"
val logstashVersion = "8.1"
val logbackVersion = "1.6.5"
val flywayVersion = "6.5.0"
val hikariVersion = "3.4.5"
val kotliqueryVersion = "1.3.1"
val httpClientVersion = "4.5.14"
val mainClass = "no.nav.medlemskap.sykepenger.brukersporsmaalhandler.ApplicationKt"

plugins {
    kotlin("jvm") version "2.4.20"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "no.nav.medlemskap"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
    maven("https://jitpack.io")
    maven("https://repo.adeo.no/repository/maven-releases")
    maven("https://repo.adeo.no/repository/nexus2-m2internal")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-kotlin:$resilience4jVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonAnnotationsVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion") {
        exclude(group = "io.netty", module = "netty-codec")
        exclude(group = "io.netty", module = "netty-codec-http")
    }
    //streams
    implementation("org.apache.kafka:kafka-streams:2.8.2")
    implementation("org.apache.kafka:kafka-clients:2.8.2")
    //streams ending
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("org.apache.httpcomponents:httpclient:$httpClientVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("org.apache.httpcomponents:httpclient:$httpClientVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:1.17.1")
    implementation("com.natpryce:konfig:$konfigVersion")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggerVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer-jvm:2.3.13")
    testImplementation(platform("org.junit:junit-bom:5.14.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.mockk:mockk:1.14.11")
    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation(platform("io.cucumber:cucumber-bom:7.34.9"))

    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
    testImplementation("org.junit.platform:junit-platform-suite")
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks {

    compileTestKotlin{
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_20)
        }
    }
    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_20)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }

    shadowJar {
        archiveBaseName.set("app")
        archiveClassifier.set("")
        archiveVersion.set("")
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to mainClass
                )
            )
        }
    }
    java{
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20

    }

    test {
        useJUnitPlatform()
        //Trengs inntil videre for bytebuddy med java 16, som brukes av mockk.
        jvmArgs = listOf("-Dnet.bytebuddy.experimental=true")
        java.targetCompatibility = JavaVersion.VERSION_20
        java.sourceCompatibility = JavaVersion.VERSION_20
        systemProperty("cucumber.junit-platform.naming-strategy", "long")
    }
}

application {
    mainClass.set("no.nav.medlemskap.sykepenger.brukersporsmaalhandler.ApplicationKt")
}
