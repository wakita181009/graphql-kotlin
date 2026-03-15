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
package com.expediagroup.graphql.server.spring

import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.config.WebFluxConfigurer
import tools.jackson.databind.ObjectMapper

@Configuration
class GraphQLServerCodecConfiguration(
    private val config: GraphQLConfigurationProperties,
    private val objectMapper: ObjectMapper,
) : WebFluxConfigurer {
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        // Default Jackson 3.x codecs are used via Spring 7.x auto-configuration.
        // Fastjson2-based HTTP codec override was removed due to incompatibility
        // with Spring 7.x / Jackson 3.x. Fastjson2 annotations on GraphQL types
        // are still used for direct (non-HTTP-codec) serialization.
    }
}
