plugins {
    kotlin("jvm") version "2.0.21"
    // bmc4j applies `java` + JUnit 5 and wires the proof runtime + the bundled engine, so a `@BmcProof`
    // is just a JUnit 5 test. Version is the release candidate — bump when the RC is published.
    id("org.bmc4j") version "0.2.0-RC1"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // The library under proof. bmc4j analyzes its SHIPPED bytecode directly (the jar-mirroring
    // rewrite makes invokedynamic/string-concat/lambdas sound) and substitutes its JDK/kotlin-stdlib
    // models — so we prove kotlinx.collections.immutable exactly as a consumer gets it, no fork.
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.8")
}
