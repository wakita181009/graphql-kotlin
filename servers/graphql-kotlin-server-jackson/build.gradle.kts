import kotlinx.benchmark.gradle.JvmBenchmarkTarget

description = "Jackson serialization support for graphql-kotlin server"

plugins {
    id("com.expediagroup.graphql.conventions")
    alias(libs.plugins.benchmark)
}

dependencies {
    api(projects.graphqlKotlinServer)
    api(libs.jackson)
    api(libs.fastjson2)
    testImplementation(libs.kotlinx.coroutines.test)
}

// Benchmarks

// Create a separate source set for benchmarks.
sourceSets.create("benchmarks")

kotlin.sourceSets.getByName("benchmarks") {
    dependencies {
        implementation(libs.kotlinx.benchmark)
        implementation(sourceSets.main.get().output)
        implementation(sourceSets.main.get().runtimeClasspath)
    }
}

benchmark {
    configurations {
        register("graphQLRequest") {
            include("com.expediagroup.graphql.server.jackson.GraphQLServerRequest*")
        }
        register("graphQLResponse") {
            include("com.expediagroup.graphql.server.jackson.GraphQLServerResponse*")
        }
    }
    targets {
        register("benchmarks") {
            this as JvmBenchmarkTarget
        }
    }
}

tasks {
    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    counter = "INSTRUCTION"
                    value = "COVEREDRATIO"
                    minimum = "0.80".toBigDecimal()
                }
                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "0.60".toBigDecimal()
                }
            }
        }
    }
}
