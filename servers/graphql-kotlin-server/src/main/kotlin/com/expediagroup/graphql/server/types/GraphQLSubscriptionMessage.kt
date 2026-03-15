/*
 * Copyright 2023 Expedia, Inc
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonDeserializer as Jackson2JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize as Jackson2JsonDeserialize
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.annotation.JsonDeserialize

const val GRAPHQL_WS_CONNECTION_INIT = "connection_init"
const val GRAPHQL_WS_CONNECTION_ACK = "connection_ack"
const val GRAPHQL_WS_PING = "ping"
const val GRAPHQL_WS_PONG = "pong"
const val GRAPHQL_WS_SUBSCRIBE = "subscribe"
const val GRAPHQL_WS_NEXT = "next"
const val GRAPHQL_WS_ERROR = "error"
const val GRAPHQL_WS_COMPLETE = "complete"
const val GRAPHQL_WS_INVALID = "invalid"

/**
 * *graphql-transport-ws* subscription protocol message
 *
 * @see <a href=https://github.com/enisdenjo/graphql-ws/blob/master/PROTOCOL.md>graphql-transport-ws protocol</a>
 */
@JsonDeserialize(using = GraphQLSubscriptionMessageDeserializer::class)
@Jackson2JsonDeserialize(using = Jackson2GraphQLSubscriptionMessageDeserializer::class)
sealed class GraphQLSubscriptionMessage {
    abstract val type: String
}

@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SubscriptionMessageConnectionInit(val payload: Any? = null) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_CONNECTION_INIT
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SubscriptionMessageConnectionAck(val payload: Any? = null) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_CONNECTION_ACK
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SubscriptionMessagePing(val payload: Any? = null) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_PING
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SubscriptionMessagePong(val payload: Any? = null) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_PONG
}

@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SubscriptionMessageSubscribe(val id: String, val payload: GraphQLRequest) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_SUBSCRIBE
}

@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SubscriptionMessageNext(val id: String, val payload: GraphQLResponse<*>) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_NEXT
}

@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SubscriptionMessageError(val id: String, val payload: List<GraphQLServerError>) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_ERROR
}

@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SubscriptionMessageComplete(val id: String) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_COMPLETE
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ValueDeserializer.None::class)
@Jackson2JsonDeserialize(using = Jackson2JsonDeserializer.None::class)
data class SubscriptionMessageInvalid(val id: String? = null, val payload: Any? = null) : GraphQLSubscriptionMessage() {
    override val type: String = GRAPHQL_WS_INVALID
}

// Jackson 3.x deserializer
class GraphQLSubscriptionMessageDeserializer : ValueDeserializer<GraphQLSubscriptionMessage>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): GraphQLSubscriptionMessage {
        val jsonNode = parser.readValueAsTree<JsonNode>()
        return when (jsonNode.get("type")?.textValue()) {
            GRAPHQL_WS_CONNECTION_INIT -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessageConnectionInit::class.java)
            GRAPHQL_WS_CONNECTION_ACK -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessageConnectionAck::class.java)
            GRAPHQL_WS_PING -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessagePing::class.java)
            GRAPHQL_WS_PONG -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessagePong::class.java)
            GRAPHQL_WS_SUBSCRIBE -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessageSubscribe::class.java)
            GRAPHQL_WS_NEXT -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessageNext::class.java)
            GRAPHQL_WS_ERROR -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessageError::class.java)
            GRAPHQL_WS_COMPLETE -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessageComplete::class.java)
            else -> ctxt.readTreeAsValue(jsonNode, SubscriptionMessageInvalid::class.java)
        }
    }
}

// Jackson 2.x deserializer (for Ktor compatibility)
class Jackson2GraphQLSubscriptionMessageDeserializer : com.fasterxml.jackson.databind.JsonDeserializer<GraphQLSubscriptionMessage>() {
    override fun deserialize(parser: com.fasterxml.jackson.core.JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): GraphQLSubscriptionMessage {
        val codec = parser.codec
        val jsonNode = codec.readTree<com.fasterxml.jackson.databind.JsonNode>(parser)
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
