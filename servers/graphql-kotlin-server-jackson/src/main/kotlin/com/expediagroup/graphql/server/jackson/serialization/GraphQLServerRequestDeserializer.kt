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

import com.expediagroup.graphql.server.types.GraphQLBatchRequest
import com.expediagroup.graphql.server.types.GraphQLRequest
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode

class GraphQLServerRequestDeserializer : JsonDeserializer<GraphQLServerRequest>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): GraphQLServerRequest {
        val codec = parser.codec
        val jsonNode = codec.readTree<JsonNode>(parser)
        return if (jsonNode.isArray) {
            codec.treeToValue(jsonNode, GraphQLBatchRequest::class.java)
        } else {
            codec.treeToValue(jsonNode, GraphQLRequest::class.java)
        }
    }
}
