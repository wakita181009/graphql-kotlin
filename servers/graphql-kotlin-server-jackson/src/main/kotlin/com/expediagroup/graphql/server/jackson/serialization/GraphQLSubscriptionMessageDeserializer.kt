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

import com.expediagroup.graphql.server.types.GRAPHQL_WS_COMPLETE
import com.expediagroup.graphql.server.types.GRAPHQL_WS_CONNECTION_ACK
import com.expediagroup.graphql.server.types.GRAPHQL_WS_CONNECTION_INIT
import com.expediagroup.graphql.server.types.GRAPHQL_WS_ERROR
import com.expediagroup.graphql.server.types.GRAPHQL_WS_NEXT
import com.expediagroup.graphql.server.types.GRAPHQL_WS_PING
import com.expediagroup.graphql.server.types.GRAPHQL_WS_PONG
import com.expediagroup.graphql.server.types.GRAPHQL_WS_SUBSCRIBE
import com.expediagroup.graphql.server.types.GraphQLSubscriptionMessage
import com.expediagroup.graphql.server.types.SubscriptionMessageComplete
import com.expediagroup.graphql.server.types.SubscriptionMessageConnectionAck
import com.expediagroup.graphql.server.types.SubscriptionMessageConnectionInit
import com.expediagroup.graphql.server.types.SubscriptionMessageError
import com.expediagroup.graphql.server.types.SubscriptionMessageInvalid
import com.expediagroup.graphql.server.types.SubscriptionMessageNext
import com.expediagroup.graphql.server.types.SubscriptionMessagePing
import com.expediagroup.graphql.server.types.SubscriptionMessagePong
import com.expediagroup.graphql.server.types.SubscriptionMessageSubscribe
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode

class GraphQLSubscriptionMessageDeserializer : JsonDeserializer<GraphQLSubscriptionMessage>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): GraphQLSubscriptionMessage {
        val codec = parser.codec
        val jsonNode = codec.readTree<JsonNode>(parser)
        return when (jsonNode.get("type")?.textValue()) {
            GRAPHQL_WS_CONNECTION_INIT -> codec.treeToValue(jsonNode, SubscriptionMessageConnectionInit::class.java)
            GRAPHQL_WS_CONNECTION_ACK -> codec.treeToValue(jsonNode, SubscriptionMessageConnectionAck::class.java)
            GRAPHQL_WS_PING -> codec.treeToValue(jsonNode, SubscriptionMessagePing::class.java)
            GRAPHQL_WS_PONG -> codec.treeToValue(jsonNode, SubscriptionMessagePong::class.java)
            GRAPHQL_WS_SUBSCRIBE -> codec.treeToValue(jsonNode, SubscriptionMessageSubscribe::class.java)
            GRAPHQL_WS_NEXT -> codec.treeToValue(jsonNode, SubscriptionMessageNext::class.java)
            GRAPHQL_WS_ERROR -> codec.treeToValue(jsonNode, SubscriptionMessageError::class.java)
            GRAPHQL_WS_COMPLETE -> codec.treeToValue(jsonNode, SubscriptionMessageComplete::class.java)
            else -> codec.treeToValue(jsonNode, SubscriptionMessageInvalid::class.java)
        }
    }
}
