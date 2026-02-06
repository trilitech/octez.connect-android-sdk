package io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network.event

import io.tezos.octezconnect.core.internal.network.HttpClient
import io.tezos.octezconnect.core.internal.network.HttpClient.Companion.get
import io.tezos.octezconnect.core.internal.network.HttpClient.Companion.put
import io.tezos.octezconnect.core.internal.network.data.ApplicationJson
import io.tezos.octezconnect.core.internal.network.data.BearerHeader
import io.tezos.octezconnect.core.network.data.HttpParameter
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.data.api.MatrixError
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.data.api.event.MatrixEventResponse
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.data.api.sync.MatrixSyncResponse
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.data.api.sync.MatrixSyncStateEvent
import io.tezos.octezconnect.transport.p2p.matrix.internal.matrix.network.MatrixService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

internal class MatrixEventService(httpClient: HttpClient) : MatrixService(httpClient) {
    private var ongoingSync: Flow<Result<MatrixSyncResponse>>? = null

    suspend fun sync(
        node: String,
        accessToken: String,
        since: String? = null,
        timeout: Long? = null,
    ): Result<MatrixSyncResponse> {
        val parameters = mutableListOf<HttpParameter>().apply {
            since?.let { add("since" to it) }
            timeout?.let { add("timeout" to it.toString()) }
        }.toList()

        return getOngoingOrRun(this::ongoingSync::get, this::ongoingSync::set) {
            withApi(node) { baseUrl ->
                httpClient.get<MatrixSyncResponse, MatrixError>(
                    baseUrl,
                    "/sync",
                    headers = listOf(BearerHeader(accessToken), ApplicationJson()),
                    parameters = parameters,
                    timeoutMillis = timeout?.times(1.5)?.toLong(),
                )
            }
        }.single()
    }

    suspend fun sendTextMessage(
        node: String,
        accessToken: String,
        roomId: String,
        txnId: String,
        message: String,
    ): Result<MatrixEventResponse> =
        sendEvent(
            node,
            accessToken,
            roomId,
            MatrixSyncStateEvent.Message.TYPE,
            txnId,
            MatrixSyncStateEvent.Message.Content(
                MatrixSyncStateEvent.Message.Content.TYPE_TEXT,
                message,
            ),
        )

    private suspend inline fun <reified T : Any> sendEvent(
        node: String,
        accessToken: String,
        roomId: String,
        eventType: String,
        txnId: String,
        content: T,
    ): Result<MatrixEventResponse> =
        withApi(node) { baseUrl ->
            httpClient.put<T, MatrixEventResponse, MatrixError>(
                baseUrl,
                "/rooms/$roomId/send/$eventType/$txnId",
                body = content,
                headers = listOf(BearerHeader(accessToken), ApplicationJson()),
            ).single()
        }

    private suspend inline fun <T : Any> getOngoingOrRun(
        getter: () -> Flow<Result<T>>?,
        crossinline setter: (Flow<Result<T>>?) -> Unit,
        action: () -> Flow<Result<T>>,
    ): Flow<Result<T>> =
        getter() ?: run {
            val scope = CoroutineScope(CoroutineName("sync")) + Dispatchers.Default
            action()
                .shareIn(scope, SharingStarted.Eagerly, replay = 1)
                .take(1)
                .onCompletion {
                    setter(null)
                    scope.cancel()
                }
                .also { setter(it) }
        }
}