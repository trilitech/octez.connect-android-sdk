package io.tezos.octezconnect.core.network.provider

import io.tezos.octezconnect.core.network.data.HttpHeader
import io.tezos.octezconnect.core.network.data.HttpParameter
import io.tezos.octezconnect.core.network.exception.HttpException
import kotlin.reflect.KClass

public interface HttpClientProvider {

    @Throws(HttpException::class)
    public suspend fun get(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        timeoutMillis: Long?,
    ): String

    @Throws(HttpException::class)
    public suspend fun post(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String? = null,
        timeoutMillis: Long?,
    ): String

    @Throws(HttpException::class)
    public suspend fun put(
        baseUrl: String,
        endpoint: String,
        headers: List<HttpHeader>,
        parameters: List<HttpParameter>,
        body: String? = null,
        timeoutMillis: Long?,
    ): String
}