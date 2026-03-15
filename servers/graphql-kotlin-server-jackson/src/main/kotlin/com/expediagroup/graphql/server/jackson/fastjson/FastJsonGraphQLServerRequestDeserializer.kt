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

package com.expediagroup.graphql.server.jackson.fastjson

import com.alibaba.fastjson2.JSONReader
import com.alibaba.fastjson2.reader.ObjectReader
import com.expediagroup.graphql.server.types.GraphQLBatchRequest
import com.expediagroup.graphql.server.types.GraphQLRequest
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import java.lang.reflect.Type

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
