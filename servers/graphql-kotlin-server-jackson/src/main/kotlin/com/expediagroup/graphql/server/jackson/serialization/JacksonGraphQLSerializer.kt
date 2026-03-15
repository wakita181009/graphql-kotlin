/*
 * Copyright 2026 Expedia, Inc
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

package com.expediagroup.graphql.server.jackson.serialization

import com.expediagroup.graphql.server.serialization.GraphQLSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * Jackson-based implementation of [GraphQLSerializer].
 *
 * Uses an [ObjectMapper] configured with the [GraphQLKotlinJacksonModule] for proper
 * serialization/deserialization of GraphQL types.
 */
class JacksonGraphQLSerializer(
    private val objectMapper: ObjectMapper = defaultObjectMapper()
) : GraphQLSerializer {

    override fun serialize(value: Any): String = objectMapper.writeValueAsString(value)

    override fun <T : Any> deserialize(content: String, type: Class<T>): T = objectMapper.readValue(content, type)

    companion object {
        fun defaultObjectMapper(): ObjectMapper = jacksonObjectMapper().apply {
            registerModule(GraphQLKotlinJacksonModule())
        }
    }
}
