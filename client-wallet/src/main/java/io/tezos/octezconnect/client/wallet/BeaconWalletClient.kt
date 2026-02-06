package io.tezos.octezconnect.client.wallet

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.client.wallet.internal.di.ExtendedDependencyRegistry
import io.tezos.octezconnect.client.wallet.internal.di.extend
import io.tezos.octezconnect.core.builder.InitBuilderWithBaseStorage
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.client.BeaconConsumer
import io.tezos.octezconnect.core.data.AppMetadata
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.data.Peer
import io.tezos.octezconnect.core.exception.BeaconException
import io.tezos.octezconnect.core.internal.BeaconConfiguration
import io.tezos.octezconnect.core.internal.controller.connection.ConnectionController
import io.tezos.octezconnect.core.internal.controller.message.MessageController
import io.tezos.octezconnect.core.internal.crypto.Crypto
import io.tezos.octezconnect.core.internal.data.BeaconApplication
import io.tezos.octezconnect.core.internal.serializer.Serializer
import io.tezos.octezconnect.core.internal.storage.StorageManager
import io.tezos.octezconnect.core.internal.utils.IdentifierCreator
import io.tezos.octezconnect.core.internal.utils.dependencyRegistry
import io.tezos.octezconnect.core.internal.utils.mapException
import io.tezos.octezconnect.core.message.AcknowledgeBeaconResponse
import io.tezos.octezconnect.core.message.BeaconMessage
import io.tezos.octezconnect.core.message.BeaconRequest
import io.tezos.octezconnect.core.message.BeaconResponse
import io.tezos.octezconnect.core.scope.BeaconScope
import io.tezos.octezconnect.core.transport.data.PairingRequest
import io.tezos.octezconnect.core.transport.data.PairingResponse
import kotlinx.coroutines.flow.Flow

/**
 * Asynchronous client that communicates with dApps.
 *
 * ### Receive requests
 *
 * To start receiving requests from connected dApps, call [connect] and subscribe to the returned [Flow], for example:
 *
 * ```
 * beaconWalletClient.connect()
 *     .mapNotNull { result -> result.getOrNull() }
 *     .collect { request ->
 *         println("Received $request")
 *     }
 * ```
 *
 * ### Respond to a request
 *
 * Use the ID [id][BeaconMessage.id] of the received message to construct a [response][BeaconResponse]
 * and send it back with [respond], for example:
 *
 * ```
 * // request = PermissionBeaconRequest(...)
 * val response = PermissionBeaconResponse(
 *     id = request.id,
 *     publicKey = "...",
 *     network = request.network,
 *     scopes = request.scopes,
 * )
 *
 * try {
 *     beaconWalletClient.respond(response)
 * } catch (e: Exception) {
 *     println("Failed to respond, error: $e")
 * }
 * ```
 *
 * ### Connect with a new dApp
 *
 * To connect with a new peer, create a new [peer][Peer] instance and register it in the [client][BeaconWalletClient]
 * using [addPeers], for example:
 *
 * ```
 * val peer = P2pPeerInfo(
 *     name = "Example dApp",
 *     publicKey = "...",
 *     relayServer = "...",
 * )
 *
 * beaconWalletClient.addPeers(peer)
 * ```
 *
 * ### Disconnect from a dApp
 *
 * To disconnect from a peer, unregister it in the [client][BeaconWalletClient] with [removePeers].
 */
public class BeaconWalletClient @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) constructor(
    app: BeaconApplication,
    beaconId: String,
    beaconScope: BeaconScope,
    connectionController: ConnectionController,
    messageController: MessageController,
    storageManager: StorageManager,
    crypto: Crypto,
    serializer: Serializer,
    configuration: BeaconConfiguration,
    identifierCreator: IdentifierCreator
) : BeaconClient<BeaconRequest>(app, beaconId, beaconScope, connectionController, messageController, storageManager, crypto, serializer, configuration, identifierCreator), BeaconConsumer {

    /**
     * Sends the [response] in reply to a previously received request.
     *
     * @throws [BeaconException] if processing and sending the [response] failed.
     */
    @Throws(BeaconException::class)
    override suspend fun respond(response: BeaconResponse) {
        send(response, isTerminal = true)
            .takeIfNotIgnored()
            ?.mapException { BeaconException.from(it) }
            ?.getOrThrow()
    }

    /**
     * Responds to the pairing [request] and finalizes the process. Returns
     * the pairing response, which, depending on the transport used, may require additional handling.
     *
     * @throws [BeaconException] if the process failed.
     */
    @Throws(BeaconException::class)
    override suspend fun pair(request: PairingRequest): PairingResponse =
        connectionController.pair(request)
            .mapException { BeaconException.from(it) }
            .getOrThrow()

    /**
     * Responds to the pairing [request] and finalizes the process. Returns
     * the pairing response, which, depending on the transport used, may require additional handling.
     *
     * @throws [BeaconException] if the process failed.
     */
    @Throws(BeaconException::class)
    override suspend fun pair(request: String): PairingResponse {
        val request = deserializePairingData<PairingRequest>(request)
        return pair(request)
    }

    /**
     * Returns a list of stored app metadata.
     */
    public suspend fun getAppMetadata(): List<AppMetadata> =
        storageManager.getAppMetadata()

    /**
     * Returns the first app metadata that matches the specified [senderId]
     * or `null` if no such app metadata was found.
     */
    public suspend fun getAppMetadataFor(senderId: String): AppMetadata? =
        storageManager.findAppMetadata { it.senderId == senderId }

    /**
     * Removes app metadata that matches the specified [senderIds].
     */
    public suspend fun removeAppMetadataFor(vararg senderIds: String) {
        storageManager.removeAppMetadata { senderIds.contains(it.senderId) }
    }

    /**
     * Removes app metadata that matches the specified [senderIds].
     */
    public suspend fun removeAppMetadataFor(senderIds: List<String>) {
        storageManager.removeAppMetadata { senderIds.contains(it.senderId) }
    }

    /**
     * Removes the specified [appMetadata].
     */
    public suspend fun removeAppMetadata(vararg appMetadata: AppMetadata) {
        removeAppMetadata(appMetadata.toList())
    }

    /**
     * Removes the specified [appMetadata].
     */
    public suspend fun removeAppMetadata(appMetadata: List<AppMetadata>) {
        storageManager.removeAppMetadata(appMetadata)
    }

    /**
     * Removes all app metadata.
     */
    public suspend fun removeAllAppMetadata() {
        storageManager.removeAppMetadata()
    }

    protected override suspend fun processMessage(origin: Connection.Id, message: BeaconMessage): Result<Unit> =
        when (message) {
            is BeaconRequest -> acknowledge(message)
            else -> super.processMessage(origin, message)
        }

    protected override suspend fun transformMessage(message: BeaconMessage): BeaconRequest? =
        when (message) {
            is BeaconRequest -> message
            else -> null
        }

    private suspend fun acknowledge(request: BeaconRequest): Result<Unit> {
        val acknowledgeResponse = AcknowledgeBeaconResponse.from(request, beaconId)
        return send(acknowledgeResponse, isTerminal = false)
    }

    public companion object {}

    /**
     * Asynchronous builder for [BeaconWalletClient].
     *
     * @constructor Creates a builder configured with the specified application [name].
     */
    public class Builder(name: String, clientId: String? = null) : InitBuilderWithBaseStorage<BeaconWalletClient, Builder>(name, BeaconScope(clientId)) {

        private var _extendedDependencyRegistry: ExtendedDependencyRegistry? = null
        private val extendedDependencyRegistry: ExtendedDependencyRegistry
            get() = _extendedDependencyRegistry ?: dependencyRegistry(beaconScope).extend().also { _extendedDependencyRegistry = it }

        /**
         * Creates a new instance of [BeaconWalletClient].
         */
        override suspend fun createInstance(configuration: BeaconConfiguration): BeaconWalletClient =
            extendedDependencyRegistry.walletClient(connections, configuration)
    }
}

/**
 * Creates a new instance of [BeaconWalletClient] with the specified application [name] and configured with [builderAction].
 *
 * @see [BeaconWalletClient.Builder]
 */
public suspend fun BeaconWalletClient(
    name: String,
    clientId: String? = null,
    builderAction: BeaconWalletClient.Builder.() -> Unit = {},
): BeaconWalletClient = BeaconWalletClient.Builder(name, clientId).apply(builderAction).build()

