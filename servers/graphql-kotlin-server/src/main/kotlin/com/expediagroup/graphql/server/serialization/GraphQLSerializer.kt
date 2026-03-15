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

package com.expediagroup.graphql.server.serialization

/**
 * Abstraction for serializing and deserializing GraphQL messages.
 *
 * This interface decouples the shared server module from any specific JSON library
 * (e.g., Jackson 2, Jackson 3, kotlinx.serialization), allowing each framework integration
 * to provide its own serialization implementation.
 */
interface GraphQLSerializer {
    /**
     * Serialize the given value to a JSON string.
     */
    fun serialize(value: Any): String

    /**
     * Deserialize the given JSON string to an instance of the specified type.
     */
    fun <T : Any> deserialize(content: String, type: Class<T>): T
}

/**
 * Convenience inline function for deserialization with reified type parameter.
 */
inline fun <reified T : Any> GraphQLSerializer.deserialize(content: String): T = deserialize(content, T::class.java)
