/*
 * Copyright 2025 Expedia, Inc
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

package com.expediagroup.graphql.server.types

/**
 * GraphQL server response abstraction that provides a convenient way to handle both single and batch responses.
 */
sealed class GraphQLServerResponse

/**
 * Wrapper that holds single GraphQLResponse to an HTTP request.
 */
data class GraphQLResponse<T>(
    val data: T? = null,
    val errors: List<GraphQLServerError>? = null,
    val extensions: Map<Any, Any?>? = null
) : GraphQLServerResponse()

/**
 * Wrapper that holds list of GraphQLResponses that were processed together within a single HTTP request.
 */
data class GraphQLBatchResponse(val responses: List<GraphQLResponse<*>>) : GraphQLServerResponse()
