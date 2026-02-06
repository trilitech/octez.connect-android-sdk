package io.tezos.octezconnect.core.internal.serializer.provider

import io.tezos.octezconnect.core.internal.utils.Base58Check
import io.tezos.octezconnect.core.internal.utils.decodeFromString
import io.tezos.octezconnect.core.internal.utils.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

internal class Base58CheckSerializerProvider(private val base58Check: Base58Check, private val json: Json) : SerializerProvider {
    override fun <T : Any> serialize(message: T, sourceClass: KClass<T>): String {
        val jsonEncoded = json.encodeToString(message, sourceClass)

        return base58Check.encode(jsonEncoded.encodeToByteArray()).getOrThrow()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> deserialize(message: String, targetClass: KClass<T>): T {
        val decoded = base58Check.decode(message).getOrThrow()
        val jsonEncoded = decoded.decodeToString()

        return json.decodeFromString(jsonEncoded, targetClass)
    }
}