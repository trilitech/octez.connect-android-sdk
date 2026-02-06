package io.tezos.octezconnect.core.internal.serializer.provider

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.internal.utils.decodeFromString
import io.tezos.octezconnect.core.internal.utils.encodeToString
import io.tezos.octezconnect.core.internal.utils.failWith
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class MockSerializerProvider(private val json: Json, public var shouldFail: Boolean = false) : SerializerProvider {
    @Throws(Exception::class)
    override fun <T : Any> serialize(message: T, sourceClass: KClass<T>): String =
        if (shouldFail) failWith()
        else json.encodeToString(message, sourceClass)

    override fun <T : Any> deserialize(message: String, targetClass: KClass<T>): T =
        if (shouldFail) failWith()
        else json.decodeFromString(message, targetClass)
}