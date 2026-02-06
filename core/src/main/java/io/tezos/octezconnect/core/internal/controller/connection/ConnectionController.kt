package io.tezos.octezconnect.core.internal.controller.connection

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.exception.ConnectionException
import io.tezos.octezconnect.core.exception.MultipleConnectionException
import io.tezos.octezconnect.core.internal.message.BeaconIncomingConnectionMessage
import io.tezos.octezconnect.core.internal.message.BeaconOutgoingConnectionMessage
import io.tezos.octezconnect.core.internal.message.IncomingConnectionTransportMessage
import io.tezos.octezconnect.core.internal.message.SerializedIncomingConnectionMessage
import io.tezos.octezconnect.core.internal.message.SerializedOutgoingConnectionMessage
import io.tezos.octezconnect.core.internal.serializer.Serializer
import io.tezos.octezconnect.core.internal.transport.Transport
import io.tezos.octezconnect.core.internal.utils.asyncMap
import io.tezos.octezconnect.core.internal.utils.failWithIllegalArgument
import io.tezos.octezconnect.core.internal.utils.failWithTransportNotSupported
import io.tezos.octezconnect.core.internal.utils.failure
import io.tezos.octezconnect.core.internal.utils.flatMap
import io.tezos.octezconnect.core.internal.utils.runCatchingFlat
import io.tezos.octezconnect.core.internal.utils.success
import io.tezos.octezconnect.core.transport.data.PairingMessage
import io.tezos.octezconnect.core.transport.data.PairingRequest
import io.tezos.octezconnect.core.transport.data.PairingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlin.reflect.KClass

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class ConnectionController internal constructor(private val transports: List<Transport>, private val serializer: Serializer) {

    public fun subscribe(): Flow<Result<BeaconIncomingConnectionMessage>> =
        transports
            .map { it.subscribe() }
            .merge()
            .map { BeaconIncomingConnectionMessage.fromResult(it)}

    public suspend fun send(message: BeaconOutgoingConnectionMessage): Result<Unit> =
        runCatchingFlat {
            val serializedContent = serializer.serialize(message.content).getOrThrow()
            val serializedMessage = SerializedOutgoingConnectionMessage(message.destination, serializedContent)

            return transports
                .asyncMap { it.send(serializedMessage) }
                .foldIndexed(Result.success()) { index, acc, next ->
                    acc.concat(next, transports[index].type)
                }
        }

    public suspend fun pair(connectionType: Connection.Type): Flow<Result<PairingMessage>> {
        val transport = transports.firstOrNull { it.type == connectionType } ?: failWithTransportNotSupported(connectionType)
        return transport.pair()
    }

    public suspend fun pair(request: PairingRequest): Result<PairingResponse> =
        runCatchingFlat {
            val transport = transports.firstOrNull { it.supportsPairing(request) } ?: failWithTransportNotSupported()
            return transport.pair(request)
        }

    private fun BeaconIncomingConnectionMessage.Companion.fromResult(
        connectionMessage: Result<IncomingConnectionTransportMessage>
    ): Result<BeaconIncomingConnectionMessage> = connectionMessage.flatMap { message ->
        val content = runCatchingFlat {
            when (message) {
                is SerializedIncomingConnectionMessage -> serializer.deserialize(message.content)
                is BeaconIncomingConnectionMessage -> Result.success(message.content)
            }
        }

        content.map { BeaconIncomingConnectionMessage(message.origin, it) }
    }

    private fun Result<Unit>.concat(other: Result<Unit>, connectionType: Connection.Type): Result<Unit> {
        onSuccess {
            other.onSuccess { return Result.success() }
            other.onFailure { otherException -> return Result.failure(ConnectionException.from(connectionType, otherException)) }
        }
        onFailure { thisException ->
            other.onSuccess { return Result.failure(ConnectionException.from(connectionType, thisException)) }
            other.onFailure { otherException ->
                val concat = thisException.concat(ConnectionException.from(connectionType, otherException))
                Result.failure<Unit>(concat)
            }
        }

        return Result.failure()
    }

    private fun Throwable.concat(other: Throwable): Throwable? =
        when {
            this is MultipleConnectionException && other is MultipleConnectionException ->
                MultipleConnectionException(errors + other.errors.distinctBy { it.type })

            this is MultipleConnectionException && other is ConnectionException ->
                MultipleConnectionException(errors + other)

            this is ConnectionException && other is ConnectionException ->
                MultipleConnectionException(listOf(this, other))

            this is ConnectionException && other is MultipleConnectionException ->
                MultipleConnectionException(other.errors + this)

            else -> null
        }

    private fun failWithUnexpectedConnectionTransportMessage(type: KClass<out IncomingConnectionTransportMessage>): Nothing =
        failWithIllegalArgument("Unexpected ConnectionTransportMessageType $type")
}