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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule as Jackson2KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue as jackson2ReadValue
import org.junit.jupiter.api.Test
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import tools.jackson.module.kotlin.readValue
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GraphQLSubscriptionMessageTest {

    private val mapper = JsonMapper.builder().addModule(KotlinModule.Builder().build()).build()
    private val jackson2Mapper = ObjectMapper().registerModule(Jackson2KotlinModule.Builder().build())

    @Test
    fun `verify connection_init deserialization`() {
        val input = """{"type":"connection_init"}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageConnectionInit)
        assertNull(message.payload)
    }

    @Test
    fun `verify connection_ack deserialization`() {
        val input = """{"type":"connection_ack"}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageConnectionAck)
        assertNull(message.payload)
    }

    @Test
    fun `verify ping deserialization`() {
        val input = """{"type":"ping"}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessagePing)
    }

    @Test
    fun `verify pong deserialization`() {
        val input = """{"type":"pong"}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessagePong)
    }

    @Test
    fun `verify subscribe deserialization`() {
        val input = """{"type":"subscribe","id":"1","payload":{"query":"subscription { foo }"}}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageSubscribe)
        assertEquals("1", message.id)
        assertEquals("subscription { foo }", message.payload.query)
    }

    @Test
    fun `verify next deserialization`() {
        val input = """{"type":"next","id":"1","payload":{"data":{"foo":"bar"}}}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageNext)
        assertEquals("1", message.id)
    }

    @Test
    fun `verify error deserialization`() {
        val input = """{"type":"error","id":"1","payload":[{"message":"test error"}]}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageError)
        assertEquals("1", message.id)
        assertEquals(1, message.payload.size)
        assertEquals("test error", message.payload[0].message)
    }

    @Test
    fun `verify complete deserialization`() {
        val input = """{"type":"complete","id":"1"}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageComplete)
        assertEquals("1", message.id)
    }

    @Test
    fun `verify invalid message deserialization`() {
        val input = """{"type":"unknown_type","id":"1"}"""
        val message = mapper.readValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageInvalid)
    }

    @Test
    fun `jackson2 verify connection_init deserialization`() {
        val input = """{"type":"connection_init"}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageConnectionInit)
        assertNull(message.payload)
    }

    @Test
    fun `jackson2 verify connection_ack deserialization`() {
        val input = """{"type":"connection_ack"}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageConnectionAck)
        assertNull(message.payload)
    }

    @Test
    fun `jackson2 verify ping deserialization`() {
        val input = """{"type":"ping"}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessagePing)
    }

    @Test
    fun `jackson2 verify pong deserialization`() {
        val input = """{"type":"pong"}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessagePong)
    }

    @Test
    fun `jackson2 verify subscribe deserialization`() {
        val input = """{"type":"subscribe","id":"1","payload":{"query":"subscription { foo }"}}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageSubscribe)
        assertEquals("1", message.id)
        assertEquals("subscription { foo }", message.payload.query)
    }

    @Test
    fun `jackson2 verify next deserialization`() {
        val input = """{"type":"next","id":"1","payload":{"data":{"foo":"bar"}}}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageNext)
        assertEquals("1", message.id)
    }

    @Test
    fun `jackson2 verify error deserialization`() {
        val input = """{"type":"error","id":"1","payload":[{"message":"test error"}]}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageError)
        assertEquals("1", message.id)
        assertEquals(1, message.payload.size)
        assertEquals("test error", message.payload[0].message)
    }

    @Test
    fun `jackson2 verify complete deserialization`() {
        val input = """{"type":"complete","id":"1"}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageComplete)
        assertEquals("1", message.id)
    }

    @Test
    fun `jackson2 verify invalid message deserialization`() {
        val input = """{"type":"unknown_type","id":"1"}"""
        val message = jackson2Mapper.jackson2ReadValue<GraphQLSubscriptionMessage>(input)
        assertTrue(message is SubscriptionMessageInvalid)
    }
}
