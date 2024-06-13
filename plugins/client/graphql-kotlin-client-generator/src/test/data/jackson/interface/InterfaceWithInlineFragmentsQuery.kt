package com.expediagroup.graphql.generated

import com.expediagroup.graphql.client.Generated
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import com.expediagroup.graphql.generated.interfacewithinlinefragmentsquery.BasicInterface
import com.fasterxml.jackson.`annotation`.JsonProperty
import kotlin.String
import kotlin.reflect.KClass

public const val INTERFACE_WITH_INLINE_FRAGMENTS_QUERY: String =
    "query InterfaceWithInlineFragmentsQuery {\n  interfaceQuery {\n    __typename\n    id\n    name\n    ... on FirstInterfaceImplementation {\n      intValue\n    }\n    ... on SecondInterfaceImplementation {\n      floatValue\n    }\n  }\n}"

@Generated
public class InterfaceWithInlineFragmentsQuery :
    GraphQLClientRequest<InterfaceWithInlineFragmentsQuery.Result> {
  override val query: String = INTERFACE_WITH_INLINE_FRAGMENTS_QUERY

  override val operationName: String = "InterfaceWithInlineFragmentsQuery"

  override fun responseType(): KClass<InterfaceWithInlineFragmentsQuery.Result> =
      InterfaceWithInlineFragmentsQuery.Result::class

  @Generated
  public data class Result(
    /**
     * Query returning an interface
     */
    @get:JsonProperty(value = "interfaceQuery")
    public val interfaceQuery: BasicInterface,
  )
}
