package io.tezos.octezconnect.blockchain.tezos.extension

import io.tezos.octezconnect.blockchain.tezos.data.TezosNetwork
import io.tezos.octezconnect.blockchain.tezos.data.TezosPermission
import io.tezos.octezconnect.blockchain.tezos.data.operation.TezosOperation
import io.tezos.octezconnect.blockchain.tezos.message.request.BroadcastTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.request.OperationTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.request.PermissionTezosRequest
import io.tezos.octezconnect.blockchain.tezos.message.request.SignPayloadTezosRequest
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.client.BeaconProducer
import io.tezos.octezconnect.core.data.Connection
import io.tezos.octezconnect.core.data.SigningType

// -- request --

public suspend fun <T> T.requestTezosPermission(
    network: TezosNetwork = TezosNetwork.Mainnet(),
    scopes: List<TezosPermission.Scope> = listOf(TezosPermission.Scope.OperationRequest, TezosPermission.Scope.Sign),
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = PermissionTezosRequest(network, scopes, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestTezosOperation(
    operationDetails: List<TezosOperation> = emptyList(),
    network: TezosNetwork? = null,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = OperationTezosRequest(operationDetails, network, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestTezosSignPayload(
    signingType: SigningType,
    payload: String,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = SignPayloadTezosRequest(signingType, payload, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestTezosBroadcast(
    signedTransaction: String,
    network: TezosNetwork? = null,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = BroadcastTezosRequest(signedTransaction, network, this, transportType)
    request(permissionRequest)
}