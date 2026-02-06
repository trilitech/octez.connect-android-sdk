package io.tezos.octezconnect.blockchain.tezos.message.request

import androidx.annotation.RestrictTo
import io.tezos.octezconnect.blockchain.tezos.Tezos
import io.tezos.octezconnect.blockchain.tezos.extension.ownAppMetadata
import io.tezos.octezconnect.blockchain.tezos.data.TezosAppMetadata
import io.tezos.octezconnect.blockchain.tezos.data.TezosNetwork
import io.tezos.octezconnect.blockchain.tezos.data.operation.TezosOperation
import io.tezos.octezconnect.blockchain.tezos.internal.TezosBeaconConfiguration
import io.tezos.octezconnect.blockchain.tezos.internal.utils.getNetworkFor
import io.tezos.octezconnect.blockchain.tezos.message.response.BroadcastTezosResponse
import io.tezos.octezconnect.blockchain.tezos.message.response.OperationTezosResponse
import io.tezos.octezconnect.blockchain.tezos.message.response.SignPayloadTezosResponse
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.client.BeaconProducer
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.data.SigningType
import io.tezos.octezconnect.core.internal.utils.failWithActiveAccountNotSet
import io.tezos.octezconnect.core.message.BlockchainBeaconRequest

/**
 * Base class for requests specific to Tezos used in the Beacon communication.
 */
public sealed class BlockchainTezosRequest : BlockchainBeaconRequest() {
    public companion object {}
}

/**
 * Message requesting the broadcast of the given [Tezos operations][operationDetails].
 * The operations may be only partially filled by the dApp and lack certain information.
 *
 * Expects [OperationTezosResponse] as a response.
 *
 * @property [id] The value that identifies this request.
 * @property [version] The message version.
 * @property [blockchainIdentifier] The unique name of the blockchain that specifies this request.
 * @property [senderId] The value that identifies the sender of this request.
 * @property [appMetadata] The metadata describing the dApp asking for the broadcast. May be `null` if the [senderId] is unknown.
 * @property [origin] The origination data of this request.
 * @property [accountId] The account identifier of the account that is requested to handle this request. May be `null`.
 * @property [network] The network on which the operations should be broadcast.
 * @property [operationDetails] Tezos operations which should be broadcast.
 * @property [sourceAddress] The address of the Tezos account that is requested to broadcast the operations.
 */
public data class OperationTezosRequest internal constructor(
    override val id: String,
    override val version: String,
    override val blockchainIdentifier: String,
    override val senderId: String,
    override val appMetadata: TezosAppMetadata?,
    override val origin: Connection.Id,
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) override val destination: Connection.Id?,
    override val accountId: String?,
    public val network: TezosNetwork,
    public val operationDetails: List<TezosOperation>,
    public val sourceAddress: String,
) : BlockchainTezosRequest() {
    public companion object {}
}

public suspend fun <T> OperationTezosRequest(
    operationDetails: List<TezosOperation> = emptyList(),
    network: TezosNetwork? = null,
    producer: T,
    transportType: Connection.Type = Connection.Type.P2P,
) : OperationTezosRequest where T : BeaconClient<*>, T : BeaconProducer {
    val requestMetadata = producer.prepareRequest(transportType)
    val account = requestMetadata.account ?: failWithActiveAccountNotSet()
    val network = network ?: producer.getNetworkFor(account.accountId)

    return OperationTezosRequest(
        id = requestMetadata.id,
        version = TezosBeaconConfiguration.MESSAGE_VERSION,
        blockchainIdentifier = Tezos.IDENTIFIER,
        senderId = requestMetadata.senderId,
        appMetadata = producer.ownAppMetadata(),
        origin = requestMetadata.origin,
        destination = requestMetadata.destination,
        accountId = account.accountId,
        network = network,
        operationDetails = operationDetails,
        sourceAddress = account.address,
    )
}

/**
 * Message requesting the signature of the given [payload].
 *
 * Expects [SignPayloadTezosResponse] as a response.
 *
 * @property [id] The value that identifies this request.
 * @property [version] The message version.
 * @property [blockchainIdentifier] The unique name of the blockchain that specifies this request.
 * @property [senderId] The value that identifies the sender of this request.
 * @property [appMetadata] The metadata describing the dApp asking for the signature. May be `null` if the [senderId] is unknown.
 * @property [origin] The origination data of this request.
 * @property [accountId] The account identifier of the account that is requested to handle this request. May be `null`.
 * @property [signingType] The requested type of signature. The client MUST fail if cannot provide the specified signature.
 * @property [payload] The payload to be signed.
 * @property [sourceAddress] The address of the account with which the payload should be signed.
 */
public data class SignPayloadTezosRequest internal constructor(
    override val id: String,
    override val version: String,
    override val blockchainIdentifier: String,
    override val senderId: String,
    override val appMetadata: TezosAppMetadata?,
    override val origin: Connection.Id,
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) override val destination: Connection.Id?,
    override val accountId: String?,
    public val signingType: SigningType,
    public val payload: String,
    public val sourceAddress: String,
) : BlockchainTezosRequest() {
    public companion object {}
}

public suspend fun <T> SignPayloadTezosRequest(
    signingType: SigningType,
    payload: String,
    producer: T,
    transportType: Connection.Type = Connection.Type.P2P,
) : SignPayloadTezosRequest where T : BeaconClient<*>, T : BeaconProducer {
    val requestMetadata = producer.prepareRequest(transportType)

    return SignPayloadTezosRequest(
        id = requestMetadata.id,
        version = TezosBeaconConfiguration.MESSAGE_VERSION,
        blockchainIdentifier = Tezos.IDENTIFIER,
        senderId = requestMetadata.senderId,
        appMetadata = producer.ownAppMetadata(),
        origin = requestMetadata.origin,
        destination = requestMetadata.destination,
        accountId = requestMetadata.account?.accountId ?: failWithActiveAccountNotSet(),
        signingType = signingType,
        payload = payload,
        sourceAddress = requestMetadata.account?.address ?: failWithActiveAccountNotSet(),
    )
}

/**
 * Message requesting the broadcast of the given [transaction][signedTransaction].
 *
 * Expects [BroadcastTezosResponse] as a response.
 *
 * @property [id] The value that identifies this request.
 * @property [version] The message version.
 * @property [blockchainIdentifier] The unique name of the blockchain that specifies this request.
 * @property [senderId] The value that identifies the sender of this request.
 * @property [appMetadata] The metadata describing the dApp asking for the broadcast. May be `null` if the [senderId] is unknown.
 * @property [origin] The origination data of this request.
 * @property [accountId] The account identifier of the account that is requested to handle this request. May be `null`.
 * @property [network] The network on which the transaction should be broadcast.
 * @property [signedTransaction] The transaction to be broadcast.
 */
public data class BroadcastTezosRequest internal constructor(
    override val id: String,
    override val version: String,
    override val blockchainIdentifier: String,
    override val senderId: String,
    override val appMetadata: TezosAppMetadata?,
    override val origin: Connection.Id,
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP) override val destination: Connection.Id?,
    override val accountId: String?,
    public val network: TezosNetwork,
    public val signedTransaction: String,
) : BlockchainTezosRequest() {
    public companion object {}
}

public suspend fun <T> BroadcastTezosRequest(
    signedTransaction: String,
    network: TezosNetwork? = null,
    producer: T,
    transportType: Connection.Type = Connection.Type.P2P,
) : BroadcastTezosRequest where T : BeaconClient<*>, T : BeaconProducer {
    val requestMetadata = producer.prepareRequest(transportType)
    val account = requestMetadata.account ?: failWithActiveAccountNotSet()
    val network = network ?: producer.getNetworkFor(account.accountId)

    return BroadcastTezosRequest(
        id = requestMetadata.id,
        version = TezosBeaconConfiguration.MESSAGE_VERSION,
        blockchainIdentifier = Tezos.IDENTIFIER,
        senderId = requestMetadata.senderId,
        appMetadata = producer.ownAppMetadata(),
        origin = requestMetadata.origin,
        destination = requestMetadata.destination,
        accountId = account.accountId,
        network = network,
        signedTransaction = signedTransaction,
    )
}