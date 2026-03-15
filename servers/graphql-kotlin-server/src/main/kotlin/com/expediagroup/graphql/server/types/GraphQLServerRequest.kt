/*
 * Copyright 2024 Expedia, Inc
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

import com.alibaba.fastjson2.JSONReader
import com.alibaba.fastjson2.annotation.JSONType
import com.alibaba.fastjson2.reader.ObjectReader
import com.expediagroup.graphql.server.extensions.readAs
import com.expediagroup.graphql.server.extensions.readAsArray
import com.expediagroup.graphql.server.types.serializers.FastJsonIncludeNonNullProperty
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonDeserializer as Jackson2JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize as Jackson2JsonDeserialize
import java.lang.reflect.Type
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.annotation.JsonDeserialize

/**
 * GraphQL server request abstraction that provides a convenient way to handle both single and batch requests.
 */
@JsonDeserialize(using = GraphQLServerRequestDeserializer::class)
@Jackson2JsonDeserialize(using = Jackson2GraphQLServerRequestDeserializer::class)
@JSONType(deserializer = FastJsonGraphQLServerRequestDeserializer::class)
sealed class GraphQLServerRequest

/**
 * Wrapper that holds single GraphQLRequest to be processed within an HTTP request.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JSONType(serializeFilters = [FastJsonIncludeNonNullProperty::class])
data class GraphQLRequest(
    val query: String = "",
    val operationName: String? = null,
    val variables: Map<String, Any?>? = null,
    val extensions: Map<String, Any?>? = null
) : GraphQLServerRequest()

/**
 * Wrapper that holds list of GraphQLRequests to be processed together within a single HTTP request.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
data class GraphQLBatchRequest @JsonCreator constructor(@get:JsonValue val requests: List<GraphQLRequest>) : GraphQLServerRequest() {
    constructor(vararg requests: GraphQLRequest) : this(requests.toList())
}

// Jackson 3.x deserializer
class GraphQLServerRequestDeserializer : ValueDeserializer<GraphQLServerRequest>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): GraphQLServerRequest {
        val jsonNode = parser.readValueAsTree<JsonNode>()
        return if (jsonNode.isArray) {
            ctxt.readTreeAsValue(jsonNode, GraphQLBatchRequest::class.java)
        } else {
            ctxt.readTreeAsValue(jsonNode, GraphQLRequest::class.java)
        }
    }
}

// Jackson 2.x deserializer (for Ktor compatibility)
class Jackson2GraphQLServerRequestDeserializer : Jackson2JsonDeserializer<GraphQLServerRequest>() {
    override fun deserialize(parser: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): GraphQLServerRequest {
        val codec = parser.codec
        val jsonNode = codec.readTree<com.fasterxml.jackson.databind.JsonNode>(parser)
        return if (jsonNode.isArray) {
            codec.treeToValue(jsonNode, GraphQLBatchRequest::class.java)
        } else {
            codec.treeToValue(jsonNode, GraphQLRequest::class.java)
        }
    }
}

object FastJsonGraphQLServerRequestDeserializer : ObjectReader<GraphQLServerRequest> {
    override fun readObject(
        jsonReader: JSONReader?,
        fieldType: Type?,
        fieldName: Any?,
        features: Long
    ): GraphQLServerRequest? {
        if (jsonReader == null || jsonReader.nextIfNull()) return null
        return if (jsonReader.isArray) {
            GraphQLBatchRequest(jsonReader.readAsArray<GraphQLRequest>())
        } else {
            jsonReader.readAs<GraphQLRequest>()
        }
    }
}
