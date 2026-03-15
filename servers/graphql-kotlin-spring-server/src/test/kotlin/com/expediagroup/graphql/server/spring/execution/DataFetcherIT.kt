/*
 * Copyright 2021 Expedia, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.expediagroup.graphql.server.spring.execution

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.server.operations.Query
import com.expediagroup.graphql.server.types.GraphQLRequest
import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import graphql.language.StringValue
import graphql.language.Value
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate
import java.util.Locale
import kotlin.reflect.KType
import kotlin.reflect.jvm.jvmErasure
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.module.SimpleModule
import tools.jackson.module.kotlin.KotlinModule

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["graphql.packages=com.expediagroup.graphql.server.spring.execution"]
)
@EnableAutoConfiguration
@AutoConfigureWebTestClient
class DataFetcherIT(@Autowired private val testClient: WebTestClient) {

    @Test
    fun `verify custom jackson bindings work with function data fetcher`() {
        val request = GraphQLRequest(query = """query { postWidget(widget: { id: 1, date: "2020-01-01" }) }""")
        testClient.post()
            .uri("/graphql")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectBody()
            .jsonPath("$.data.postWidget").isEqualTo("JUNIT: widget [ID: 1, date: 2020-01-01]")
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun query(): Query = CustomQuery()

        @Bean
        fun customHook(): SchemaGeneratorHooks = object : SchemaGeneratorHooks {
            override fun willGenerateGraphQLType(type: KType): GraphQLType? = if (type.jvmErasure == LocalDate::class) {
                localDateType
            } else {
                super.willGenerateGraphQLType(type)
            }
        }

        @Bean
        fun objectMapper(): ObjectMapper = JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .addModule(
                SimpleModule().apply {
                    addDeserializer(
                        LocalDate::class.java,
                        object : ValueDeserializer<LocalDate>() {
                            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDate =
                                LocalDate.parse(p.getText())
                        },
                    )
                    addSerializer(
                        LocalDate::class.java,
                        object : ValueSerializer<LocalDate>() {
                            override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializationContext) {
                                gen.writeString(value.toString())
                            }
                        },
                    )
                }
            )
            .build()

        private val localDateType = GraphQLScalarType.newScalar()
            .name("LocalDate")
            .description("A type representing local date")
            .coercing(LocalDateCoercing)
            .build()

        private object LocalDateCoercing : Coercing<LocalDate, String> {

            override fun serialize(dataFetcherResult: Any, graphQLContext: GraphQLContext, locale: Locale): String =
                dataFetcherResult.toString()

            override fun parseValue(input: Any, graphQLContext: GraphQLContext, locale: Locale): LocalDate =
                try {
                    LocalDate.parse(serialize(input, graphQLContext, locale))
                } catch (e: Exception) {
                    throw CoercingParseValueException("Cannot parse value $input to LocalDate", e)
                }

            override fun parseLiteral(input: Value<*>, variables: CoercedVariables, graphQLContext: GraphQLContext, locale: Locale): LocalDate =
                try {
                    LocalDate.parse((input as? StringValue)?.value)
                } catch (e: Exception) {
                    throw CoercingParseLiteralException("Cannot parse literal $input to LocalDate", e)
                }
        }
    }

    class CustomQuery : Query {
        fun postWidget(widget: Widget): String = "JUNIT: widget [ID: ${widget.id}, date: ${widget.date}]"
    }

    data class Widget(
        val id: Int,
        val date: LocalDate
    )
}
