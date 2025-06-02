import nu.studer.gradle.jooq.JooqGenerate
import org.testcontainers.containers.PostgreSQLContainer

buildscript {
    dependencies {
        classpath("org.testcontainers:postgresql:1.21.1")
        classpath("org.flywaydb:flyway-database-postgresql:11.9.0")
    }
}

plugins {
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"

    id("com.github.spotbugs") version "6.1.13"
    id("com.diffplug.spotless") version "7.0.4"

    id("org.flywaydb.flyway") version "11.8.2"
    id("nu.studer.jooq") version "10.1"

    id("java")
    id("idea")

    kotlin("jvm") version "2.1.21"
}

group = "org.poc"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

val resilience4jVersion = "1.7.1"
val retrofitVersion = "3.0.0"
val postgresqlVersion = "42.7.6"
val mockitoVersion = "5.18.0"
val testcontainersVersion = "1.21.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // AOP is needed for the `Observed` annotation
    implementation("org.springframework.boot:spring-boot-starter-aop") // 1

    // Tracing dependencies after Spring Boot 3
    implementation("io.micrometer:micrometer-tracing-bridge-brave") // 2
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    // Without this dependency actuator does not provide a /actuator/prometheus endpoint.
    implementation("io.micrometer:micrometer-registry-prometheus")

    jooqGenerator("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("io.github.resilience4j:resilience4j-retrofit:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-retry:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-circuitbreaker:$resilience4jVersion")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")
    implementation("com.google.guava:guava:33.4.8-jre")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage:junit-vintage-engine")
    }
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner:4.3.0")
    testImplementation("io.rest-assured:rest-assured:5.5.5")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")

    spotbugsSlf4j("org.slf4j:slf4j-simple")
}

val postgresqlSQLContainer =
    tasks.create("postgresqlContainer") {
        if (project.gradle.startParameter.taskNames
                .any { it.contains("flyway|Jooq".toRegex()) }
        ) {
            val instance =
                PostgreSQLContainer("postgres:16.4")
                    .withDatabaseName("poc_crud")
                    .apply { start() }
            extra.apply {
                set("jdbc_url", instance.jdbcUrl)
                set("username", instance.username)
                set("password", instance.password)
            }
        }
    }

// Database migration by Gradle for manual run `./gradlew flywayMigrate` or for future pipeline before service rollout
flyway {
    url = postgresqlSQLContainer.extra.takeIf { it.has("jdbc_url") }.let { it?.get("jdbc_url").toString() }
    user = postgresqlSQLContainer.extra.takeIf { it.has("username") }.let { it?.get("username").toString() }
    password = postgresqlSQLContainer.extra.takeIf { it.has("password") }.let { it?.get("password").toString() }
}

jooq {
    configurations {
        create("main") {
            // name of the jOOQ configuration
            generateSchemaSourceOnCompilation.set(false) // default (can be omitted)
            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = flyway.url
                    user = flyway.user
                    password = flyway.password
                }
                generator.apply {
                    name = "org.jooq.codegen.JavaGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "org.poc.jooq_retrofit.repository.jooq"
                        directory = "src/main/jooq"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

// configure jOOQ task such that it only executes when something has changed that potentially affects the generated JOOQ sources
// - the jOOQ configuration has changed (Jdbc, Generator, Strategy, etc.)
// - the classpath used to execute the jOOQ generation tool has changed (jOOQ library, database driver, strategy classes, etc.)
// - the schema files from which the schema is generated and which is used by jOOQ to generate the sources have changed (scripts added, modified, etc.)
tasks.named<JooqGenerate>("generateJooq").configure {
    (launcher::set)(
        javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(21))
        },
    )
    // ensure database schema has been prepared by Flyway before generating the jOOQ sources
    dependsOn(tasks.named("postgresqlContainer"))
    dependsOn(tasks.named("flywayMigrate"))

    // declare Flyway migration scripts as inputs on the jOOQ task
    inputs
        .files(fileTree("src/main/resources/db/migration"))
        .withPropertyName("migrations")
        .withPathSensitivity(PathSensitivity.RELATIVE)

    // make jOOQ task participate in incremental builds and build caching
    allInputsDeclared.set(true)
    outputs.cacheIf { true }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Clean code configurations

spotbugs {
    excludeFilter.set(file(".github/linters/spotbugs-exclude.xml"))
}

spotless {
    java {
        googleJavaFormat()
    }
    kotlin {
        target("**/*.kts", "**/*.kt")
        ktlint()
    }
}
