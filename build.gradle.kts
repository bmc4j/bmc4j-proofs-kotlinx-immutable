plugins {
    // Kotlin 2.4 is the consumer floor for bmc4j 0.4.x (its plugin/KGP uses 2.4-era compiler APIs).
    kotlin("jvm") version "2.4.0"
    // bmc4j applies `java` + JUnit 5 and wires the proof runtime + the bundled engine, so a `@BmcProof`
    // is just a JUnit 5 test. A GitHub Packages SNAPSHOT (tag-shortsha) of current main — it carries the
    // kotlin-facade models (ArraysKt/MathKt/CharsKt) that the structural proofs here need, which the
    // 0.4.x Central releases predate. Bump to a Central release once one ships with those models.
    id("org.bmc4j") version "0.4.4-d37d746"
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
