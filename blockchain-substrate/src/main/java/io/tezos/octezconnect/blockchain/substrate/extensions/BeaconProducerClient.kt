package io.tezos.octezconnect.blockchain.substrate.extensions

import io.tezos.octezconnect.blockchain.substrate.data.SubstrateNetwork
import io.tezos.octezconnect.blockchain.substrate.data.SubstratePermission
import io.tezos.octezconnect.blockchain.substrate.data.SubstrateSignerPayload
import io.tezos.octezconnect.blockchain.substrate.message.request.PermissionSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.SignPayloadReturnSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.SignPayloadSubmitAndReturnSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.SignPayloadSubmitSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.TransferReturnSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.TransferSubmitAndReturnSubstrateRequest
import io.tezos.octezconnect.blockchain.substrate.message.request.TransferSubmitSubstrateRequest
import io.tezos.octezconnect.core.client.BeaconClient
import io.tezos.octezconnect.core.client.BeaconProducer
import io.tezos.octezconnect.core.data.Connection

// -- request --

public suspend fun <T> T.requestSubstratePermission(
    networks: List<SubstrateNetwork>,
    scopes: List<SubstratePermission.Scope> = listOf(SubstratePermission.Scope.SignPayloadJson, SubstratePermission.Scope.SignPayloadRaw, SubstratePermission.Scope.Transfer),
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = PermissionSubstrateRequest(networks, scopes, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestSubstrateTransferSubmit(
    amount: String,
    recipient: String,
    network: SubstrateNetwork? = null,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = TransferSubmitSubstrateRequest(amount, recipient, network, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestSubstrateTransferSubmitAndReturn(
    amount: String,
    recipient: String,
    network: SubstrateNetwork? = null,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = TransferSubmitAndReturnSubstrateRequest(amount, recipient, network, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestSubstrateTransferReturn(
    amount: String,
    recipient: String,
    network: SubstrateNetwork? = null,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = TransferReturnSubstrateRequest(amount, recipient, network, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestSubstrateSignPayloadSubmit(
    address: String,
    payload: SubstrateSignerPayload,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = SignPayloadSubmitSubstrateRequest(address, payload, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestSubstrateSignPayloadSubmitAndReturn(
    address: String,
    payload: SubstrateSignerPayload,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = SignPayloadSubmitAndReturnSubstrateRequest(address, payload, this, transportType)
    request(permissionRequest)
}

public suspend fun <T> T.requestSubstrateSignPayloadReturn(
    address: String,
    payload: SubstrateSignerPayload,
    transportType: Connection.Type = Connection.Type.P2P,
) where T : BeaconProducer, T : BeaconClient<*> {
    val permissionRequest = SignPayloadReturnSubstrateRequest(address, payload, this, transportType)
    request(permissionRequest)
}
