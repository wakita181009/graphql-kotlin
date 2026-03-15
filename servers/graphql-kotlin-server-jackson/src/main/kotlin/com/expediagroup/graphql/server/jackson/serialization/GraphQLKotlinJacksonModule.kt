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

import com.expediagroup.graphql.server.jackson.mixins.GraphQLBatchRequestMixin
import com.expediagroup.graphql.server.jackson.mixins.GraphQLBatchResponseMixin
import com.expediagroup.graphql.server.jackson.mixins.GraphQLRequestMixin
import com.expediagroup.graphql.server.jackson.mixins.GraphQLResponseMixin
import com.expediagroup.graphql.server.jackson.mixins.GraphQLServerErrorMixin
import com.expediagroup.graphql.server.jackson.mixins.GraphQLServerRequestMixin
import com.expediagroup.graphql.server.jackson.mixins.GraphQLServerResponseMixin
import com.expediagroup.graphql.server.jackson.mixins.GraphQLSourceLocationMixin
import com.expediagroup.graphql.server.jackson.mixins.GraphQLSubscriptionMessageMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessageCompleteMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessageConnectionAckMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessageConnectionInitMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessageErrorMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessageInvalidMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessageNextMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessagePingMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessagePongMixin
import com.expediagroup.graphql.server.jackson.mixins.SubscriptionMessageSubscribeMixin
import com.expediagroup.graphql.server.types.GraphQLBatchRequest
import com.expediagroup.graphql.server.types.GraphQLBatchResponse
import com.expediagroup.graphql.server.types.GraphQLRequest
import com.expediagroup.graphql.server.types.GraphQLResponse
import com.expediagroup.graphql.server.types.GraphQLServerError
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import com.expediagroup.graphql.server.types.GraphQLServerResponse
import com.expediagroup.graphql.server.types.GraphQLSourceLocation
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
import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * Jackson module that registers all necessary mixins for GraphQL Kotlin server types.
 *
 * This module decouples the serialization configuration from the shared server types,
 * allowing different Jackson versions or serialization libraries to be used by different
 * framework integrations (e.g., Spring with Jackson 3, Ktor with Jackson 2).
 */
class GraphQLKotlinJacksonModule : SimpleModule("GraphQLKotlinModule") {
    override fun setupModule(context: SetupContext) {
        super.setupModule(context)

        // Request types
        context.setMixInAnnotations(GraphQLServerRequest::class.java, GraphQLServerRequestMixin::class.java)
        context.setMixInAnnotations(GraphQLRequest::class.java, GraphQLRequestMixin::class.java)
        context.setMixInAnnotations(GraphQLBatchRequest::class.java, GraphQLBatchRequestMixin::class.java)

        // Response types
        context.setMixInAnnotations(GraphQLServerResponse::class.java, GraphQLServerResponseMixin::class.java)
        context.setMixInAnnotations(GraphQLResponse::class.java, GraphQLResponseMixin::class.java)
        context.setMixInAnnotations(GraphQLBatchResponse::class.java, GraphQLBatchResponseMixin::class.java)

        // Error types
        context.setMixInAnnotations(GraphQLServerError::class.java, GraphQLServerErrorMixin::class.java)
        context.setMixInAnnotations(GraphQLSourceLocation::class.java, GraphQLSourceLocationMixin::class.java)

        // Subscription message types
        context.setMixInAnnotations(GraphQLSubscriptionMessage::class.java, GraphQLSubscriptionMessageMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessageConnectionInit::class.java, SubscriptionMessageConnectionInitMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessageConnectionAck::class.java, SubscriptionMessageConnectionAckMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessagePing::class.java, SubscriptionMessagePingMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessagePong::class.java, SubscriptionMessagePongMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessageSubscribe::class.java, SubscriptionMessageSubscribeMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessageNext::class.java, SubscriptionMessageNextMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessageError::class.java, SubscriptionMessageErrorMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessageComplete::class.java, SubscriptionMessageCompleteMixin::class.java)
        context.setMixInAnnotations(SubscriptionMessageInvalid::class.java, SubscriptionMessageInvalidMixin::class.java)
    }
}
