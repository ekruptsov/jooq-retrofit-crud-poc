
plugins {
    id("com.github.spotbugs") version "5.0.6"
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.diffplug.spotless") version "6.14.0"
    id("java")
    id("idea")
}

group = "org.template.test.assignment"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val resilience4jVersion = "1.7.1"
val retrofitVersion = "2.9.0"
val mockitoVersion = "5.1.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // AOP is needed for the `Observed` annotation
    implementation("org.springframework.boot:spring-boot-starter-aop") // 1

    // Tracing dependencies after Spring Boot 3
    implementation("io.micrometer:micrometer-tracing-bridge-brave") // 2
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    // Without this dependency actuator does not provide a /actuator/prometheus endpoint.
    implementation("io.micrometer:micrometer-registry-prometheus")

    implementation("io.github.resilience4j:resilience4j-retrofit:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:$resilience4jVersion")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    implementation("com.google.guava:guava:31.1-jre")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage:junit-vintage-engine")
    }
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")

    spotbugsSlf4j("org.slf4j:slf4j-simple")
}

dependencyManagement {
    imports {
        mavenBom("com.fasterxml.jackson:jackson-bom:2.14.2")
    }
}

// Clean code configurations

spotless {
    java {
        googleJavaFormat()
    }
}
